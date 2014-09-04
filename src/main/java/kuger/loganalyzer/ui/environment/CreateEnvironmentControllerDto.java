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
    private SinkDto sink;

    public void setSink(SinkDto sink) {
        this.sink = sink;
    }

    public void addPipeline(PipelineWidgetControllerDto dto) {
        pipelines.add(dto);
    }

    public Stream<FilterDto> getDistinctFilterStream() {
        Stream<FilterDto> result = Stream.empty();
        for (PipelineWidgetControllerDto pipeline : pipelines) {
                result = Stream.concat(result, pipeline.filterStream());
        }
        return result.distinct();
    }
}
