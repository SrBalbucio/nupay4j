package balbucio.com.nubank.model.refund;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class NuRefund {

    private String refundId;
    private NuRefundStatus status;
    private String dueDate;
    private String transactionRefundId;
    private RefundAmount amount;
    private NuRefundError error;
    private String source;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ToString
    public class RefundAmount{
        private String currency;
        private double value;
    }
}
