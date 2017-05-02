package logic;

class NoBackupsException extends Exception {

    NoBackupsException() {
        super();
    }

    NoBackupsException(String message) {
        super(message);
    }
}
