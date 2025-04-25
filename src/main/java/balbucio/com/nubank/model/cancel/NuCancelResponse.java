package balbucio.com.nubank.model.cancel;

import lombok.NonNull;

public class NuCancelResponse {

    @NonNull
    private String pspReferenceId;
    @NonNull
    private String referenceId;
    @NonNull
    private NuCancelStatus status;
    private NuCheckoutCancelCode code;
    private String message;
}
