package balbucio.com.nubank.exception;

public class NuInternalError extends NuException{

    public NuInternalError(String msg) {
        super("Could not perform a task or request: "+msg);
    }
}
