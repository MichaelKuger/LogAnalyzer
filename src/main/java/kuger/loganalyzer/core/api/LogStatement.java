package kuger.loganalyzer.core.api;

import java.time.Instant;

public class LogStatement implements Comparable<LogStatement> {

    private Instant timestamp;
    private String message;

    public LogStatement(Instant timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    @Override
    public int compareTo(LogStatement o) {
        return timestamp.compareTo(o.timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogStatement that = (LogStatement) o;

        if (!message.equals(that.message)) return false;
        if (!timestamp.equals(that.timestamp)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LogStatement{" +
                "timestamp=" + timestamp +
                ", message=" + message +
                '}';
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
