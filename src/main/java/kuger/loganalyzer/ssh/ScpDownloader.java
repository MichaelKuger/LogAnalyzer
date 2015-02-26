package kuger.loganalyzer.ssh;

import com.jcraft.jsch.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ScpDownloader extends Thread {

    private final SshConfig sshConfig;
    private final OutputStream pos;
    private final String filePath;

    public ScpDownloader(SshConfig sshConfig, String filePath, OutputStream pos) {
        this.sshConfig = sshConfig;
        this.filePath = filePath;
        this.pos = pos;
        setDaemon(true);
    }

    @Override
    public void run() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(sshConfig.getUsername(), sshConfig.getHost(), sshConfig.getPort());

            // username and password will be given via UserInfo interface.
            UserInfo userInfo = new SshUserInfo();
            session.setUserInfo(userInfo);
            session.connect();

            // exec 'scp -f rfile' remotely
            String command = "scp -f " + filePath;
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] buf = new byte[1024];

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            while (true) {
                int c = checkAck(in);
                if (c != 'C') {
                    break;
                }

                // read '0644 '
                in.read(buf, 0, 5);

                long filesize = 0L;
                while (true) {
                    if (in.read(buf, 0, 1) < 0) {
                        // error
                        break;
                    }
                    if (buf[0] == ' ') break;
                    filesize = filesize * 10L + (long) (buf[0] - '0');
                }

                String file = null;
                for (int i = 0; ; i++) {
                    in.read(buf, i, 1);
                    if (buf[i] == (byte) 0x0a) {
                        file = new String(buf, 0, i);
                        break;
                    }
                }

                //System.out.println("filesize="+filesize+", file="+file);

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();

                // read a content of lfile
                int foo;
                while (true) {
                    if (buf.length < filesize) foo = buf.length;
                    else foo = (int) filesize;
                    foo = in.read(buf, 0, foo);
                    if (foo < 0) {
                        // error
                        break;
                    }
                    bos.write(buf, 0, foo);
                    pos.write(buf, 0, foo);
                    System.out.println("written!");
                    filesize -= foo;
                    if (filesize == 0L) break;
                }
                if (checkAck(in) != 0) {
                    System.exit(0);
                }

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();
            }

            session.disconnect();
        } catch (IOException | JSchException e) {
            e.printStackTrace();
        } finally {
            try {
                pos.flush();
                pos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println(new String(bos.toByteArray()));
                System.out.println("SCP client finished.");
            }
        }
    }

    static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuilder sb = new StringBuilder();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }
}
