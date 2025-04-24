package balbucio.com.nubank.model.invoice;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
@ToString
public class NuShopper {

    private String reference;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String document;
    @NonNull
    private DocumentType documentType = DocumentType.CPF;
    @NonNull
    private String email;
    private Phone phone;
    private String ip;
    private String locale;

    public enum DocumentType {
        CPF
    }

    @AllArgsConstructor
    @Data
    @ToString
    public static class Phone {
        private String country;
        private String number;
    }
}
