package kuger.loganalyzer.core.api;

import java.io.File;

public class FileInputContainer implements Input {

    private final File file;
    private final String timestampPattern;
    private final InputIdentifier inputIdentifier;

    public FileInputContainer(File file, String timestampPattern, InputIdentifier inputIdentifier) {
        this.file = file;
        this.timestampPattern = timestampPattern;
        this.inputIdentifier = inputIdentifier;
    }

    public File getFile() {
        return file;
    }

    public String getTimestampPattern() {
        return timestampPattern;
    }

    @Override
    public String toString() {
        return "FileInputContainer{" +
                "file=" + file +
                ", timestampPattern='" + timestampPattern + '\'' +
                '}';
    }

    @Override
    public InputIdentifier getIdentifier() {
        return inputIdentifier;
    }
}
