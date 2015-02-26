package kuger.loganalyzer.core.api;

import java.io.Reader;

public abstract class InputContainer implements Input {

    private final String timestampPattern;
    private final InputIdentifier inputIdentifier;

    protected InputContainer(String timestampPattern, InputIdentifier inputIdentifier) {
        this.timestampPattern = timestampPattern;
        this.inputIdentifier = inputIdentifier;
    }

    public String getTimestampPattern() {
        return timestampPattern;
    }

    public abstract Reader getReader();

    @Override
    public final InputIdentifier getIdentifier() {
        return inputIdentifier;
    }

    @Override
    public String toString() {
        return "InputContainer{" +
                "timestampPattern='" + timestampPattern + '\'' +
                ", inputIdentifier=" + inputIdentifier +
                '}';
    }
}
