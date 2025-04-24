package balbucio.com.nubank.model.invoice;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@ToString
@Builder
public class NuPaymentMethod {

    @NonNull
    private Type type;
    private FundingSource fundingSource;
    private AuthorizationType authorizationType;

    public enum Type {
        NUPAY
    }

    public enum FundingSource {
        CREDIT, DEBIT, CREDIT_WITH_ADDITIONAL_LIMIT;
    }

    public enum AuthorizationType {
        MANUALLY, PRE_AUTHORIZED;
    }
}
