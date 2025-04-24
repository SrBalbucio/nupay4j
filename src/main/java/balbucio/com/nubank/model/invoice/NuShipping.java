package balbucio.com.nubank.model.invoice;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@Builder
@ToString
public class NuShipping {

    private double value;
    private String company;
    private NuAddress address;
}
