package kuger.loganalyzer.core.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class FileInputContainer extends InputContainer {

    private final File file;

    public FileInputContainer(File file, String timestampPattern, InputIdentifier inputIdentifier) {
        super(timestampPattern, inputIdentifier);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public Reader getReader() {
        try {
            return new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "FileInputContainer{" +
                "file=" + file +
                '}';
    }
}
