package balbucio.com.nubank.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NuError {

    SYSTEM_ERROR("The system can not process this payment now. Please try again later."),
    CANCELLED_BY_USER(" The user cancelled the payment."),
    CANCELLED_BY_TIMEOUT("This payment was cancelled due to timeout."),
    CANCELLED_BY_INSTITUTION("The institution cancelled the payment."),
    CANCELLED_BY_SELLER("Seller has cancelled this payment."),
    REVERSED("This payment was reversed due to an internal error.");


    private final String message;
}
