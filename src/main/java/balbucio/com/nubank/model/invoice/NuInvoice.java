package balbucio.com.nubank.model.invoice;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class NuInvoice {

    @NonNull
    private String merchantOrderReference;
    @NonNull
    private String referenceId;
    @SerializedName("amount")
    @NonNull
    private Price price;
    @NonNull
    private NuShopper shopper;
    @NonNull
    private List<NuCheckoutItem> items;
    @NonNull
    private NuPaymentMethod paymentMethod;
    private String transactionId;
    private int installments;
    @NonNull
    private NuPaymentFlow paymentFlow;
    private String merchantName;
    private String storeName;
    private int delayToAutoCancel;
    private NuShipping shipping;
    private NuAddress billingAddress;
    private String orderUrl;
    private String callbackUrl;
    private String referenceDate;

    public static class Price {
        private String currency;
        private double value;
        private Details details;

        @AllArgsConstructor
        @Data
        @Builder
        @ToString
        public class Details {
            private double taxValue;
        }
    }
}
