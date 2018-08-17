package model.exceptions;

public class NoBackupsException extends Exception {

    public NoBackupsException() {
        super();
    }

    public NoBackupsException(String message) {
        super(message);
    }
}
