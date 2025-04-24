package balbucio.com.nubank.model.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class NuResponseError {

    private int status;
    private String message;
}
