package kuger.loganalyzer.ui.environment;

import java.io.File;

public class SinkDto {

    private String file;

    @Override
    public String toString() {
        return "SinkDto{" +
                "file='" + file + '\'' +
                '}';
    }

    public void setFile(File file) {
        if (file == null) {
            return;
        }
        this.file = file.getAbsolutePath();
    }
}
