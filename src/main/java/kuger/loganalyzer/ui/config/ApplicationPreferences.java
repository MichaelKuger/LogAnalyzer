package kuger.loganalyzer.ui.config;

import java.util.prefs.Preferences;

public class ApplicationPreferences {

    public static final ApplicationPreferences INSTANCE = new ApplicationPreferences();

    public static final String PIPELINE_FILE_DEFAULT_PATH = "pipeline_file_default_path";
    public static final String PIPELINE_FILE_DEFAULT_OUTPUT_PATH = "pipeline_file_default_output_path";
    public static final String SETUP_CONFIG_DEFAULT_PATH = "setup_config_default_path";

    private Preferences preferences;

    private ApplicationPreferences() {
        preferences = Preferences.userRoot().node(this.getClass().getName());
    }

    public void setPipelineFileDefaultPath(String path) {
        preferences.put(PIPELINE_FILE_DEFAULT_PATH, path);
    }

    public void setPipelineFileDefaultOutputPath(String path) {
        preferences.put(PIPELINE_FILE_DEFAULT_OUTPUT_PATH, path);
    }

    public void setSetupConfigDefaultPath(String path) {
        preferences.put(SETUP_CONFIG_DEFAULT_PATH, path);
    }

    public String getStringForProperty(String property, String defaultValue) {
        return preferences.get(property, defaultValue);
    }

}