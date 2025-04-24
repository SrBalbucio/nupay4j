package balbucio.com.nubank.model.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class NuPaymentFlow {

    private String returnUrl;
    private String cancelUrl;
}
