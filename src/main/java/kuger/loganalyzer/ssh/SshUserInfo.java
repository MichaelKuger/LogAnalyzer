package kuger.loganalyzer.ssh;

import com.jcraft.jsch.UserInfo;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SshUserInfo implements UserInfo {
    @Override
    public String getPassphrase() {
        throw new NotImplementedException();
    }

    @Override
    public String getPassword() {
        throw new NotImplementedException();
    }

    @Override
    public boolean promptPassword(String s) {
        System.out.println(s);
       return true;
    }

    @Override
    public boolean promptPassphrase(String s) {
        System.out.println(s);
        throw new NotImplementedException();
    }

    @Override
    public boolean promptYesNo(String s) {
        System.out.println(s);
        return true;
    }

    @Override
    public void showMessage(String s) {
        System.out.println(s);
        throw new NotImplementedException();
    }
}
