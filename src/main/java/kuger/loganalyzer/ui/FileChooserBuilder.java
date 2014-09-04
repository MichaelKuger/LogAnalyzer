package kuger.loganalyzer.ui;

import javafx.stage.FileChooser;
import kuger.loganalyzer.ui.config.ApplicationPreferences;

import java.io.File;

public class FileChooserBuilder {

    private FileChooser result = new FileChooser();

    public FileChooserBuilder withTitle(String title) {
        result.setTitle(title);
        return this;
    }

    public FileChooserBuilder withDefaultPath(String preferenceName) {
        String defaultPath = ApplicationPreferences.INSTANCE.getStringForProperty(preferenceName, null);
        if (defaultPath != null) {
            File selectedDir = new File(defaultPath);
            while (!selectedDir.isDirectory()) {
                selectedDir = selectedDir.getParentFile();
            }
            result.setInitialDirectory(selectedDir);
            result.setInitialFileName(defaultPath);
        }
        return this;
    }

    public FileChooser build() {
        return result;
    }
}
