package balbucio.com.nubank.model.ipn;

import balbucio.com.nubank.model.invoice.NuPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class NuNotificationStatus {

    private String pspReferenceId;
    private String referenceId;
    private String timestamp;
    private NuPaymentMethod.Type paymentMethodType;
}
