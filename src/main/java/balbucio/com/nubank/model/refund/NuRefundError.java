package balbucio.com.nubank.model.refund;

public class NuRefundError {

    private Type type;
    private String message;
    private String code;

    public enum Type{
        UNKNOWN, SYSTEM, PAYMENT_METHOD, OPERATION, INSUFFICIENT_FUNDS, FULLY_REFUNDED, MAX_NUMBER_REACHED
    }
}
