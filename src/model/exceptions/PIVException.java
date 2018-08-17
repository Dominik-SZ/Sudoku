package model.exceptions;

import java.util.function.Function;

/**
 * Should be thrown, when a method requires possibility integrity, which is not assured.
 */
public class PIVException extends Exception {

    public PIVException() {
        super();
    }

    public PIVException(String message) {
        super(message);
    }

    /**
     * Wraps the function that throws a {@link PIVException},
     * so that it still throws the exception but the compiler doesn't complain.
     *
     * @param func a {@link Function} that throws a PIVException
     * @return a function that seems not to throw a PIVException
     * (although it does :)
     * @throws PIVException if the wrapped function throws a
     *                                               PIVException
     * @see <a href="http://www.stackoverflow.com/a/27644392">http://www.stackoverflow.com/a/27644392</a>
     * @see <a href="http://www.stackoverflow.com/a/30974991">http://www.stackoverflow.com/a/30974991</a>
     */
    public static <T, R> Function<T, R> wrap(Function_WithException<T, R, PIVException> func)
            throws PIVException {
        return t -> {
            try {
                return func.apply(t);
            } catch (PIVException e) {
                // this is a hack, as the compiler does not complain
                // although we are throwing a checked exception
                throwActualException(e);
                return null;
            }
        };
    }

    /**
     * A {@link Function} that throws an exception of type {@code <E>}.
     *
     * @param <E> the type of exception that is thrown by the function
     */
    @FunctionalInterface
    public interface Function_WithException<T, R, E extends Exception> {
        R apply(T t) throws E;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Exception> void throwActualException(Exception exception) throws E {
        throw (E) exception;
    }
}
