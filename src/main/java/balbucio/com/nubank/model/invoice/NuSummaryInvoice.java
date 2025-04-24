package balbucio.com.nubank.model.invoice;

import balbucio.com.nubank.model.refund.NuRefund;
import balbucio.com.nubank.model.response.NuError;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@ToString
public class NuSummaryInvoice {

    @NonNull
    private String pspReferenceId;
    @NonNull
    private String referenceId;
    @NonNull
    private NuStatus status;
    @NonNull
    @SerializedName("amount")
    private double price;
    @NonNull
    private String timestamp;
    @NonNull
    private Payer payer;
    private NuError code;
    private String message;
    private String paymentMethodType;
    private String paymentType;
    private int installmentNumber;
    private int installmentNumberPurchase;
    private List<NuRefund> refunds;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public class Payer{
        private String id;
    }
}
