package balbucio.com.nubank.model.invoice;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class NuCheckoutItem {

    @NonNull
    private String id;
    @NonNull
    @SerializedName("value")
    private double price;
    @NonNull
    private int quantity;
    @NonNull
    private String description;
    private int discount = 0;
    private double taxAmount;
    private double amountExcludingTax;
    private double amountIncludingTax;
}
