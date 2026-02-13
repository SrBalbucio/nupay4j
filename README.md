# NuPay for Java

SDK Java não oficial para a API [NuPay for Business](https://docs.nupaybusiness.com.br/) do Nubank. Permite integrar pagamentos NuPay em aplicações Java de forma simples.

[![HyperPowered](https://img.shields.io/badge/HyperPowered-Use%20the%20official%20repository-yellow?color=%23279BF8&cacheSeconds=3600)](https://maven.hyperpowered.net/#/snapshots/balbucio/com/nubank/nupay4j/)

> **Importante:** Este projeto ainda está em desenvolvimento e **não foi testado em produção nem em Sandbox** (aguardando liberação de credenciais pela equipe do Nubank). **Não utilize em produção.**

---

## Índice

- [Requisitos](#requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Uso Rápido](#uso-rápido)
- [Funcionalidades](#funcionalidades)
- [Guia de Uso Detalhado](#guia-de-uso-detalhado)
- [Notificações (IPN)](#notificações-ipn)
- [Tratamento de Erros](#tratamento-de-erros)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Estado da Implementação](#estado-da-implementação)
- [Links Úteis](#links-úteis)
- [Licença](#licença)

---

## Requisitos

- **Java 17** ou superior
- Credenciais de API do NuPay (API Key e API Token)
- Conta no [NuPay for Business](https://nubank.com.br/empresas/nupay-empresas)

---

## Instalação

### Maven

Adicione o repositório e a dependência ao seu `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>balbucio.com.nubank</groupId>
        <artifactId>nupay4j</artifactId>
        <version>0.0.4-SNAPSHOT</version>
    </dependency>
</dependencies>
```

> **Nota:** Versões publicadas também estão disponíveis no [repositório HyperPowered](https://maven.hyperpowered.net/#/snapshots/balbucio/com/nubank/nupay4j/).

---

## Configuração

```java
import balbucio.com.nubank.NuPay;
import balbucio.com.nubank.model.config.NuPayConfig;
import balbucio.com.nubank.model.config.NuMerchantCredential;

NuMerchantCredential credential = new NuMerchantCredential(
    "sua-api-key",      // X-Merchant-Key
    "seu-api-token"     // X-Merchant-Token
);

// Para pagamentos pré-autorizados (ex.: checkout salvo no app do Nubank)
credential.setClientId("seu-client-id");

NuPayConfig config = NuPayConfig.builder()
    .credential(credential)
    .sandboxMode(true)  // true = Sandbox, false = Produção
    .build();

NuPay nuPay = new NuPay(config);
```

### Ambientes

| Ambiente  | Endpoint                              | `sandboxMode` |
|-----------|----------------------------------------|---------------|
| Sandbox   | `https://sandbox-api.spinpay.com.br/`  | `true`        |
| Produção  | `https://api.spinpay.com.br/`          | `false`       |

---

## Uso Rápido

### Criar um pagamento

```java
import balbucio.com.nubank.model.invoice.*;
import balbucio.com.nubank.model.response.NuPayCheckoutResponse;

NuInvoice invoice = NuInvoice.builder()
    .merchantOrderReference("pedido-123")
    .referenceId("ref-abc-456")
    .price(NuInvoice.Price.builder()
        .value(99.90)
        .currency("BRL")
        .build())
    .shopper(NuShopper.builder()
        .firstName("João")
        .lastName("Silva")
        .document("12345678900")
        .documentType(NuShopper.DocumentType.CPF)
        .email("joao@exemplo.com")
        .phone(NuShopper.Phone.builder().country("55").number("11999999999").build())
        .ip("192.168.1.1")
        .locale("pt-BR")
        .build())
    .items(List.of(
        NuCheckoutItem.builder()
            .id("prod-1")
            .price(99.90)
            .quantity(1)
            .description("Produto exemplo")
            .build()
    ))
    .paymentMethod(NuPaymentMethod.builder()
        .type(NuPaymentMethod.Type.NUPAY)
        .fundingSource(NuPaymentMethod.FundingSource.DEBIT)
        .authorizationType(NuPaymentMethod.AuthorizationType.MANUALLY)
        .build())
    .paymentFlow(NuPaymentFlow.builder()
        .returnUrl("https://seusite.com/retorno")
        .cancelUrl("https://seusite.com/cancelar")
        .build())
    .billingAddress(NuAddress.builder()
        .street("Rua Exemplo")
        .number("100")
        .city("São Paulo")
        .state("SP")
        .postalCode("01310100")
        .country("BRA")
        .neighborhood("Centro")
        .build())
    .build();

NuPayCheckoutResponse response = nuPay.createPayment(invoice);

// Redirecione o cliente para a URL de pagamento
String paymentUrl = response.getPaymentUrl();
String pspReferenceId = response.getPspReferenceId();
```

### Consultar status do pagamento

```java
Optional<NuSummaryInvoice> invoice = nuPay.getInvoice(pspReferenceId);
invoice.ifPresent(summary -> {
    System.out.println("Status: " + summary.getStatus());  // WAITING_PAYMENT_METHOD, COMPLETED, CANCELLED, ERROR
    System.out.println("Valor: " + summary.getPrice());
});
```

### Cancelar pagamento

```java
Optional<NuCancelResponse> cancelResponse = nuPay.cancelPayment(pspReferenceId);
```

### Criar estorno

```java
NuRefundRequest refundRequest = NuRefundRequest.builder()
    .amount(new NuRefund.RefundAmount("BRL", 50.00))
    .transactionRefundId("estorno-" + System.currentTimeMillis())
    .notes("Estorno parcial solicitado pelo cliente")
    .build();

Optional<NuRefund> refund = nuPay.createRefund(pspReferenceId, refundRequest);
```

---

## Funcionalidades

| Funcionalidade                   | Síncrono | Assíncrono | Status        |
|----------------------------------|----------|------------|---------------|
| Criar pagamento (manual)         | ✅       | ✅         | Implementado  |
| Criar pagamento pré-autorizado   | ✅       | ✅         | Implementado  |
| Consultar pagamento              | ✅       | ✅         | Implementado  |
| Cancelar pagamento               | ✅       | ✅         | Implementado  |
| Criar estorno                    | ✅       | ✅         | Implementado  |
| Consultar estorno                | ✅       | ✅         | Implementado  |
| Parser de notificações (IPN)     | ✅       | —          | Implementado  |

---

## Guia de Uso Detalhado

### Pagamento pré-autorizado

Para fluxos onde o cliente já autorizou o pagamento no app do Nubank (ex.: checkout salvo):

```java
String accessToken = "token-obtido-via-oauth2";  // Obter conforme documentação NuPay OAuth2

NuPayCheckoutResponse response = nuPay.createPreAuthorizedPayment(invoice, accessToken);
```

### Execução assíncrona

Todas as operações possuem versão assíncrona que retorna `Future`:

```java
Future<NuPayCheckoutResponse> future = nuPay.createAsyncPayment(invoice);
NuPayCheckoutResponse response = future.get(30, TimeUnit.SECONDS);

Future<Optional<NuSummaryInvoice>> invoiceFuture = nuPay.getAsyncInvoice(pspReferenceId);
Future<Optional<NuCancelResponse>> cancelFuture = nuPay.asyncCancelPayment(pspReferenceId);
Future<Optional<NuRefund>> refundFuture = nuPay.createAsyncRefund(pspReferenceId, refundRequest);
```

---

## Notificações (IPN)

O NuPay envia webhooks (IPN) quando o status do pagamento ou estorno muda. Use o `NuNotificationManager` para interpretar:

```java
NuNotificationManager manager = nuPay.getNotificationManager();

// Para mudança de status do pagamento
NuNotificationStatus status = manager.parseNotificationStatus(requestBody);
Optional<NuSummaryInvoice> invoice = manager.getInvoiceFromNotificationStatus(status);

// Para mudança de status do estorno
NuNotificationRefundStatus refundStatus = manager.parseNotificationRefundStatus(requestBody);
Optional<NuRefund> refund = manager.getRefundFromNotificationRefundStatus(refundStatus);
```

---

## Tratamento de Erros

```java
import balbucio.com.nubank.exception.NuException;
import balbucio.com.nubank.exception.NuRequestException;
import balbucio.com.nubank.exception.NuAuthorizationException;
import balbucio.com.nubank.exception.NuInternalError;

try {
    NuPayCheckoutResponse response = nuPay.createPayment(invoice);
} catch (NuRequestException e) {
    NuResponseError error = e.getError();
    System.err.println("Erro da API: " + error.getMessage() + " (status: " + error.getStatus() + ")");
} catch (NuAuthorizationException e) {
    System.err.println("Problema de autenticação");
} catch (NuInternalError e) {
    System.err.println("Erro interno: " + e.getMessage());
} catch (NuException e) {
    System.err.println("Erro NuPay: " + e.getMessage());
}
```

---

## Estrutura do Projeto

```
nupay4j/
├── src/main/java/balbucio/com/nubank/
│   ├── NuPay.java                 # Cliente principal
│   ├── builder/
│   │   └── InvoiceBuilder.java    # (em desenvolvimento)
│   ├── exception/
│   │   ├── NuException.java
│   │   ├── NuRequestException.java
│   │   ├── NuAuthorizationException.java
│   │   └── NuInternalError.java
│   ├── manager/
│   │   └── NuNotificationManager.java
│   ├── model/
│   │   ├── cancel/                # Cancelamento
│   │   ├── config/                # Configuração
│   │   ├── invoice/               # Fatura/checkout
│   │   ├── ipn/                   # Notificações
│   │   ├── refund/                # Estornos
│   │   └── response/              # Respostas da API
│   └── utils/
│       └── NuRequester.java       # Requisições HTTP
└── pom.xml
```

---

## Estado da Implementação

### Implementado

- Autenticação via API Key e Token
- Criação de pagamento (manual e pré-autorizado)
- Consulta de status do pagamento
- Cancelamento de pagamento
- Criação e consulta de estornos
- Parsing de notificações IPN
- Modo Sandbox e Produção
- Versões síncronas e assíncronas

### Pendente / Não implementado

- OAuth2 / CIBA / OTP para pagamentos pré-autorizados (uso de tokens externos)
- Geração de access/refresh tokens
- Consulta de condições de pagamento por Token ou CPF
- `InvoiceBuilder` fluente (já é possível usar `NuInvoice.builder()`)
- Testes automatizados com credenciais reais

---

## Links Úteis

- [Documentação Oficial NuPay for Business](https://docs.nupaybusiness.com.br/)
- [API Reference (OpenAPI)](https://docs.nupaybusiness.com.br/checkout/docs/openapi/index.html)
- [NuPay para Empresas](https://nubank.com.br/empresas/nupay-empresas)
- Suporte: oi-nupay@nubank.com.br

### Documentação adicional

- [Referência dos modelos](docs/MODELOS.md) — descrição detalhada dos DTOs e estruturas de dados.

---

## Licença

Este é um projeto independente e não é oficialmente vinculado ao Nubank ou NuPay.
