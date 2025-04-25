package balbucio.com.nubank.model.refund;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
@ToString
public class NuRefundRequest {

    @NonNull
    private NuRefund.RefundAmount amount;
    @NonNull
    private String transactionRefundId;
    private String notes;
}
