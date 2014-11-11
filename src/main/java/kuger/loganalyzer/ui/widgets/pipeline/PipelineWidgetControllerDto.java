package kuger.loganalyzer.ui.widgets.pipeline;

import kuger.loganalyzer.ui.widgets.filter.FilterDto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class PipelineWidgetControllerDto {

    private String inputFileName;
    private String timestampPattern;
    private List<String> filter = new ArrayList<>();

    public void setTimestampPattern(String timestampPattern) {
        this.timestampPattern = timestampPattern;
    }

    void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    void addFilter(String dto) {
        filter.add(dto);
    }

    public Stream<String> filterStream() {
        return filter.stream();
    }

    @Override
    public String toString() {
        return "PipelineWidgetControllerDto{" +
                "inputFileName='" + inputFileName + '\'' +
                ", timestampPattern='" + timestampPattern + '\'' +
                ", filter=" + filter +
                '}';
    }
}
