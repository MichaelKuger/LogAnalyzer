package kuger.loganalyzer.ui.widgets.pipeline;

import kuger.loganalyzer.ui.widgets.filter.FilterDto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class PipelineWidgetControllerDto {

    private String inputFileName;
    private String timestampPattern;
    private List<FilterDto> filter = new ArrayList<>();

    public void setTimestampPattern(String timestampPattern) {
        this.timestampPattern = timestampPattern;
    }

    void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    void addFilter(FilterDto dto) {
        filter.add(dto);
    }

    public Stream<FilterDto> filterStream() {
        return filter.stream();
    }
}
