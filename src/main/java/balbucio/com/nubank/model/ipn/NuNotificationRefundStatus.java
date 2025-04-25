package balbucio.com.nubank.model.ipn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class NuNotificationRefundStatus {

    private String pspReferenceId;
    private String transactionRefundId;
    private String refundId;
    private String timestamp;
    private String referenceId;
}
