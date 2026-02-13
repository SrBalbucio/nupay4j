package balbucio.com.nubank.model.response;

import balbucio.com.nubank.model.invoice.NuPaymentMethod;
import balbucio.com.nubank.model.invoice.NuStatus;
import lombok.Data;

@Data
public class NuPayCheckoutResponse {

    private String pspReferenceId;
    private String referenceId;
    private NuStatus status;
    private String paymentUrl;
    private NuPaymentMethod.Type paymentMethodType;
    private NuError code;
    private String message;

}
