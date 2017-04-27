package logic;

public class NoBackupsException extends Exception {

    NoBackupsException() {
        super();
    }

    NoBackupsException(String message) {
        super(message);
    }
}
