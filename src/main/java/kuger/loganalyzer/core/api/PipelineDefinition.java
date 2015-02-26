package kuger.loganalyzer.core.api;

import java.util.Arrays;

public class PipelineDefinition {

    private InputContainer inputContainer;
    private final Filter[] filters;

    public PipelineDefinition(InputContainer inputContainer, Filter... filters) {
        this.inputContainer = inputContainer;
        this.filters = filters;
    }

    public InputContainer getInputContainer() {
        return inputContainer;
    }

    public Filter[] getFilters() {
        return filters;
    }

    @Override
    public String toString() {
        return "Pipeline{" +
                "inputContainer=" + inputContainer +
                ", filters=" + Arrays.toString(filters) +
                '}';
    }
}
