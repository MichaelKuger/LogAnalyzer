package kuger.loganalyzer.util;

/**
 * Created by Michael on 25.05.2014.
 */
public class InputException extends Exception {

    public InputException(String message) {
        super(message);
    }

    public InputException(String message, Throwable cause) {
        super(message, cause);
    }
}
