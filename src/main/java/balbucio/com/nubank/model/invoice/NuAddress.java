package balbucio.com.nubank.model.invoice;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@ToString
@Builder
public class NuAddress {
    @NonNull
    private String street;
    @NonNull
    private String number;
    @NonNull
    private String postalCode;
    @NonNull
    private String city;
    @NonNull
    private String state;
    @NonNull
    private String country;
    private String complement;
    private String neighborhood;
}
