package kuger.loganalyzer.core.api;

import akka.actor.ActorSystem;
import input.StatementParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Pipeline {
    private Collection<StatementParser> fileInputList = new ArrayList<>();
    private final Collection<Sink> sinks;
    private ActorSystem actorSystem;

    public Pipeline(Sink... sinks) {
        this.sinks = Arrays.asList(sinks);
    }

    public void addFileInput(StatementParser fileInput) {
        fileInputList.add(fileInput);
    }

    public void setActorSystem(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    public void start() {
        System.out.println("Starting...");
        for (StatementParser input : fileInputList) {
            input.start();
        }
    }


    public void shutdown() {
        System.out.println("Stopping...");
        actorSystem.shutdown();
    }

    public void finished() {
        System.out.println("Pipeline finished.");
        for (Sink sink : sinks) {
            sink.finished();
        }
    }
}
