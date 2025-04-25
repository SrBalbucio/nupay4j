import balbucio.com.nubank.NuPay;
import balbucio.com.nubank.model.config.NuMerchantCredential;
import balbucio.com.nubank.model.config.NuPayConfig;
import balbucio.com.nubank.model.invoice.*;
import balbucio.com.nubank.model.response.NuPayCheckoutResponse;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NuTest {

    private NuPay nuPay;

    @BeforeAll
    public void beforeAll() {
        String apiToken = System.getenv("NU_PAY_API_TOKEN");
        String apiKey = System.getenv("NU_PAY_API_KEY");

        this.nuPay = new NuPay(NuPayConfig.builder()
                .credential(new NuMerchantCredential(apiKey, apiToken))
                .sandboxMode(true)
                .build());
    }

    @Test
    @DisplayName("Criar um pagamento")
    @Order(0)
    public void createPayment() {
        NuPayCheckoutResponse response = nuPay.createPayment(NuInvoice.builder()
                .paymentMethod(NuPaymentMethod.builder()
                        .authorizationType(NuPaymentMethod.AuthorizationType.MANUALLY)
                        .fundingSource(NuPaymentMethod.FundingSource.DEBIT)
                        .build())
                .items(Arrays.asList(
                        NuCheckoutItem.builder()
                                .id("caneca")
                                .price(15.0)
                                .description("Uma caneca muito legal!")
                                .quantity(1)
                                .build()
                ))
                .price(NuInvoice.Price.builder()
                        .value(15.0)
                        .currency("BRL")
                        .build())
                .shopper(NuShopper.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .documentType(NuShopper.DocumentType.CPF)
                        .document("64262091040")
                        .email("john.doe@example.com")
                        .phone(NuShopper.Phone.builder()
                                .country("55")
                                .number("21987654321")
                                .build())
                        .ip("255.110.231.231")
                        .locale("pt-BR")
                        .build())
                .billingAddress(NuAddress.builder()
                        .country("BRA")
                        .city("SP")
                        .number("39")
                        .neighborhood("Pinheiros")
                        .postalCode("05409000")
                        .state("SP")
                        .street("Rua Capote Valente")
                        .build())
                .merchantOrderReference("123432abc")
                .referenceId("abc2345")
                .build());

        assertNotNull(response);
    }
}
