package logic;

/**
 * Should be thrown, when a method requires possibility integrity, which is not assured.
 */
public class PossibilityIntegrityViolatedException extends Exception{

    public PossibilityIntegrityViolatedException() {
        super();
    }

    public PossibilityIntegrityViolatedException(String message) {
        super(message);
    }
}
