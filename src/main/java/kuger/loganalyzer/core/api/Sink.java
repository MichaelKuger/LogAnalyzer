package kuger.loganalyzer.core.api;

public interface Sink {

    void add(LogStatement statement);

    void finished();
}
