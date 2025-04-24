package balbucio.com.nubank.exception;

import balbucio.com.nubank.model.response.NuResponseError;
import lombok.Getter;

@Getter
public class NuRequestException extends NuException {

    private final NuResponseError error;

    public NuRequestException(NuResponseError error) {
        super("Error processing a request: " + error.getMessage());
        this.error = error;
    }
}
