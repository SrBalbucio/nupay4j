package balbucio.com.nubank.exception;

public class NuAuthorizationException extends NuException {
    public NuAuthorizationException(String message) {
        super("The server denied your credentials with a 401 error: " + message);
    }
}
