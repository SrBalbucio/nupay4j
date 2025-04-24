package balbucio.com.nubank.model.config;

import lombok.*;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NuMerchantCredential {

    @NonNull
    private String apiKey;
    @NonNull
    private String apiToken;
    private String clientId;
}
