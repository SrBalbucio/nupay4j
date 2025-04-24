package balbucio.com.nubank.model.config;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
public class NuPayConfig {

    @NonNull
    private NuMerchantCredential credential;
    private boolean sandboxMode = false;
}
