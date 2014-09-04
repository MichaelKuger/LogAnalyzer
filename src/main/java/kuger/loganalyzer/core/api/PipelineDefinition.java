package kuger.loganalyzer.core.api;

import java.util.Arrays;

public class PipelineDefinition {

    private FileInputContainer fileInput;
    private final Filter[] filters;

    public PipelineDefinition(FileInputContainer fileInput, Filter... filters) {
        this.fileInput = fileInput;
        this.filters = filters;
    }

    public FileInputContainer getFileInput() {
        return fileInput;
    }


    public Filter[] getFilters() {
        return filters;
    }

    @Override
    public String toString() {
        return "Pipeline{" +
                "fileInput=" + fileInput +
                ", filters=" + Arrays.toString(filters) +
                '}';
    }
}
