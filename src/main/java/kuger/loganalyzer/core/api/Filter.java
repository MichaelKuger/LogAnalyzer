package kuger.loganalyzer.core.api;

public interface Filter {

    boolean filter(LogStatement statement);

    String getName();
}
