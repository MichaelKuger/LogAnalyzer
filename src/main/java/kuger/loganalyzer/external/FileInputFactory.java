package kuger.loganalyzer.external;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import input.FileInput;
import kuger.loganalyzer.core.api.*;
import util.ScalaHelper;

import java.util.*;

public final class FileInputFactory {

    public static Pipeline fillSink(Collection<PipelineDefinition> pipelineDefinitions, Sink... sinks) {
        PipelineCreator creator = new PipelineCreator(pipelineDefinitions, sinks);
        return creator.createPipeline();
    }


    private static class FilterActorIdentifier {
        private final ActorRef[] downstream;
        private final String filterName;

        private FilterActorIdentifier(ActorRef[] downstream, String filtername) {
            this.downstream = downstream;
            this.filterName = filtername;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FilterActorIdentifier that = (FilterActorIdentifier) o;

            if (!Arrays.equals(downstream, that.downstream)) return false;
            if (filterName != null ? !filterName.equals(that.filterName) : that.filterName != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = downstream != null ? Arrays.hashCode(downstream) : 0;
            result = 31 * result + (filterName != null ? filterName.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "FilterActorIdentifier{" +
                    "downstream=" + Arrays.toString(downstream) +
                    ", filterName='" + filterName + '\'' +
                    '}';
        }

    }

    private static class PipelineCreator {
        private final Sink[] sinks;
        private final Collection<PipelineDefinition> pipelineDefinitions;
        private final Map<FilterActorIdentifier, ActorRef> filterActorIdentifierToActorRef = new HashMap<>();

        private PipelineCreator(Collection<PipelineDefinition> pipelineDefinition, Sink[] sinks) {
            this.pipelineDefinitions = pipelineDefinition;
            this.sinks = sinks;
        }

        protected Pipeline createPipeline() {
            System.out.println(pipelineDefinitions);
            System.out.println("Setup...");
            Pipeline pipeline = new Pipeline(sinks);
            ActorSystem actorSystem = ScalaHelper.createActorSystem("LogAnalyzerSystem");
            Collection<InputIdentifier> inputIdentifiers = collectInputIdentifiers(pipelineDefinitions);
            ActorRef controlActor = ScalaHelper.createControlActor("ControlActor", actorSystem, inputIdentifiers, pipeline);
            ActorRef sinkActor = ScalaHelper.createSinkActor("SinkActor", actorSystem, controlActor, sinks);

            for (PipelineDefinition pipelineDefinition : pipelineDefinitions) {
                List<Filter> filtersList = Arrays.asList(pipelineDefinition.getFilters());
                ListIterator<Filter> filterListIterator = filtersList.listIterator(filtersList.size());

                ActorRef[] downstream = new ActorRef[]{sinkActor};
                while (filterListIterator.hasPrevious()) {
                    Filter filter = filterListIterator.previous();
                    ActorRef filterActor;
                    filterActor = getOrCreateFilterActor(actorSystem, downstream, filter);
                    downstream = new ActorRef[]{filterActor};
                }
                FileInput fileInput = new FileInput(pipelineDefinition.getFileInput(), downstream);
                pipeline.addFileInput(fileInput);
            }

            pipeline.setActorSystem(actorSystem);
            return pipeline;
        }

        private ActorRef getOrCreateFilterActor(ActorSystem actorSystem, ActorRef[] downstream, Filter filter) {
            FilterActorIdentifier identifier = new FilterActorIdentifier(downstream, filter.getName());

            if (filterActorIdentifierToActorRef.containsKey(identifier)) {
                return filterActorIdentifierToActorRef.get(identifier);
            } else {
                System.out.println("Creating filter actor for identifier " + identifier);
                ActorRef filterActor = ScalaHelper.createFilterActor(actorSystem, filter, downstream);
                filterActorIdentifierToActorRef.put(identifier, filterActor);

                return filterActor;
            }
        }

        private Collection<InputIdentifier> collectInputIdentifiers(Collection<PipelineDefinition> pipelineDefinitions) {
            Collection<InputIdentifier> result = new ArrayList<>();
            for (PipelineDefinition pipelineDefinition : pipelineDefinitions) {
                result.add(pipelineDefinition.getFileInput().getIdentifier());
            }
            return result;
        }
    }
}
