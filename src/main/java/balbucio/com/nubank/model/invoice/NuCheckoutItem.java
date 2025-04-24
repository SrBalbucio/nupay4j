package balbucio.com.nubank.model.invoice;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class NuCheckoutItem {

    @NonNull
    private String id;
    @NonNull
    private double amount;
    @NonNull
    private int quantity;
    @NonNull
    private String description;
    private int discount = 0;
    private double taxAmount;
    private double amountExcludingTax;
    private double amountIncludingTax;
}
