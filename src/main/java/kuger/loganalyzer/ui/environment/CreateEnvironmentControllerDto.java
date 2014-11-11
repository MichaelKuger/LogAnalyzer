package kuger.loganalyzer.ui.environment;

import com.google.gson.internal.Streams;
import kuger.loganalyzer.ui.widgets.filter.FilterDto;
import kuger.loganalyzer.ui.widgets.pipeline.PipelineWidgetControllerDto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CreateEnvironmentControllerDto {

    private List<PipelineWidgetControllerDto> pipelines = new ArrayList<>();
    private List<FilterDto> filters = new ArrayList<>();
    private SinkDto sink;

    public void setSink(SinkDto sink) {
        this.sink = sink;
    }

    public void addPipeline(PipelineWidgetControllerDto dto) {
        pipelines.add(dto);
    }

    @Override
    public String toString() {
        return "CreateEnvironmentControllerDto{" +
                "pipelines=" + pipelines +
                ", filters=" + filters +
                ", sink=" + sink +
                '}';
    }

    public void addFilter(FilterDto dto) {
        filters.add(dto);
    }
}
