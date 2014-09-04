package kuger.loganalyzer;

public class LogAnalyzerException extends Exception {
    public LogAnalyzerException(String message) {
        super(message);
    }

    public LogAnalyzerException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogAnalyzerException(Throwable cause) {
        super(cause);
    }
}
