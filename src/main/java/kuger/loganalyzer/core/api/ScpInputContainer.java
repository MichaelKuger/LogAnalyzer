package kuger.loganalyzer.core.api;

import kuger.loganalyzer.ssh.ScpDownloader;
import kuger.loganalyzer.ssh.SshConfig;

import java.io.*;

public class ScpInputContainer extends InputContainer {

    private final ScpDownloader downloader;
    private final PipedInputStream pis;

    public ScpInputContainer(SshConfig sshConfig, String remoteFilePath, String timestampPattern, InputIdentifier inputIdentifier) {
        super(timestampPattern, inputIdentifier);
        try {
            PipedOutputStream pos = new PipedOutputStream();
            pis = new PipedInputStream(10 * 1024);
            pos.connect(pis);
            downloader = new ScpDownloader(sshConfig, remoteFilePath, pos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reader getReader() {
        downloader.start();
        return new InputStreamReader(pis);
    }
}
