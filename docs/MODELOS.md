# Referência dos Modelos (NuPay4J)

Este documento complementa o [README principal](../README.md) com a referência detalhada dos modelos de dados.

## Configuração

### NuPayConfig

| Campo      | Tipo                 | Obrigatório | Descrição                                    |
|------------|----------------------|-------------|----------------------------------------------|
| credential | NuMerchantCredential | Sim         | Credenciais do comerciante                   |
| sandboxMode| boolean              | Não         | `true` = Sandbox, `false` = Produção (padrão)|

### NuMerchantCredential

| Campo    | Tipo   | Obrigatório | Descrição                                    |
|----------|--------|-------------|----------------------------------------------|
| apiKey   | String | Sim         | Chave de API (header X-Merchant-Key)         |
| apiToken | String | Sim         | Token de API (header X-Merchant-Token)       |
| clientId | String | Não         | Obrigatório para pagamentos pré-autorizados  |

---

## Checkout / Pagamento

### NuInvoice

Modelo principal para criar um pagamento.

| Campo                | Tipo                | Obrigatório | Descrição                        |
|----------------------|---------------------|-------------|----------------------------------|
| merchantOrderReference | String            | Sim         | Referência do pedido no seu sistema |
| referenceId          | String              | Sim         | ID único da transação            |
| price                | NuInvoice.Price     | Sim         | Valor e moeda                    |
| shopper              | NuShopper           | Sim         | Dados do comprador               |
| items                | List&lt;NuCheckoutItem&gt; | Sim | Itens do carrinho                |
| paymentMethod        | NuPaymentMethod     | Sim         | Método de pagamento              |
| paymentFlow          | NuPaymentFlow       | Sim         | URLs de retorno/cancelamento     |
| billingAddress       | NuAddress           | Não         | Endereço de cobrança             |
| shipping             | NuShipping          | Não         | Dados de entrega                 |
| delayToAutoCancel    | int                 | Não         | Tempo em segundos para auto-cancelar |
| callbackUrl          | String              | Não         | URL para webhook de notificação  |
| orderUrl             | String              | Não         | URL da página do pedido          |
| merchantName         | String              | Não         | Nome do comerciante              |
| storeName            | String              | Não         | Nome da loja                     |

### NuInvoice.Price

| Campo   | Tipo   | Descrição  |
|---------|--------|------------|
| currency| String | Ex: "BRL"  |
| value   | double | Valor      |
| details | Details| Opcional   |

### NuShopper

| Campo      | Tipo            | Obrigatório | Descrição          |
|------------|-----------------|-------------|--------------------|
| firstName  | String          | Sim         | Nome               |
| lastName   | String          | Sim         | Sobrenome          |
| document   | String          | Sim         | CPF (somente números) |
| documentType | DocumentType  | Sim         | CPF                |
| email      | String          | Sim         | E-mail             |
| phone      | Phone           | Não         | Telefone           |
| ip         | String          | Não         | IP do comprador    |
| locale     | String          | Não         | Ex: "pt-BR"        |
| reference  | String          | Não         | Referência interna |

### NuCheckoutItem

| Campo   | Tipo   | Obrigatório | Descrição  |
|---------|--------|-------------|------------|
| id      | String | Sim         | ID do item |
| price   | double | Sim         | Preço unitário |
| quantity| int    | Sim         | Quantidade |
| description | String | Sim      | Descrição  |
| discount| int    | Não         | Desconto   |

### NuPaymentMethod

| Campo            | Tipo             | Obrigatório | Descrição                          |
|------------------|------------------|-------------|------------------------------------|
| type             | Type             | Sim         | NUPAY                              |
| fundingSource    | FundingSource    | Não         | CREDIT, DEBIT, CREDIT_WITH_ADDITIONAL_LIMIT |
| authorizationType| AuthorizationType| Não         | MANUALLY ou PRE_AUTHORIZED         |

### NuPaymentFlow

| Campo    | Tipo  | Descrição                    |
|----------|-------|------------------------------|
| returnUrl| String| URL após pagamento concluído  |
| cancelUrl| String| URL se o usuário cancelar    |

### NuAddress

| Campo      | Tipo   | Obrigatório | Descrição     |
|------------|--------|-------------|---------------|
| street     | String | Sim         | Rua           |
| number     | String | Sim         | Número        |
| postalCode | String | Sim         | CEP           |
| city       | String | Sim         | Cidade        |
| state      | String | Sim         | Estado (UF)   |
| country    | String | Sim         | Código país (BRA) |
| complement | String | Não         | Complemento   |
| neighborhood | String | Não       | Bairro        |

---

## Respostas

### NuPayCheckoutResponse

| Campo           | Tipo              | Descrição                    |
|-----------------|-------------------|------------------------------|
| pspReferenceId  | String            | ID do pagamento no NuPay     |
| referenceId     | String            | Sua referência               |
| status          | NuStatus          | Status inicial               |
| paymentUrl      | String            | URL para redirecionar o cliente |
| paymentMethodType | Type            | Tipo de pagamento            |
| code            | NuError           | Código de erro (se houver)   |
| message         | String            | Mensagem (se houver)         |

### NuSummaryInvoice

Resposta da consulta de status.

| Campo     | Tipo   | Descrição                |
|-----------|--------|--------------------------|
| pspReferenceId | String | ID do pagamento      |
| referenceId    | String | Sua referência       |
| status         | NuStatus | WAITING_PAYMENT_METHOD, COMPLETED, CANCELLED, ERROR |
| price          | double | Valor                  |
| timestamp      | String | Data/hora              |
| refunds        | List   | Lista de estornos      |

### NuStatus

- `WAITING_PAYMENT_METHOD` - Aguardando pagamento
- `COMPLETED` - Concluído
- `CANCELLED` - Cancelado
- `ERROR` - Erro

---

## Estorno

### NuRefundRequest

| Campo             | Tipo                | Obrigatório | Descrição             |
|-------------------|---------------------|-------------|------------------------|
| amount            | NuRefund.RefundAmount | Sim      | Valor a estornar       |
| transactionRefundId | String            | Sim         | ID único do estorno    |
| notes             | String              | Não         | Observações            |

### NuRefund.RefundAmount

| Campo   | Tipo   | Descrição  |
|---------|--------|------------|
| currency| String | Ex: "BRL"  |
| value   | double | Valor      |

### NuRefund

| Campo              | Tipo          | Descrição      |
|--------------------|---------------|----------------|
| refundId           | String        | ID do estorno  |
| status             | NuRefundStatus| Status         |
| transactionRefundId| String        | Sua referência |
| amount             | RefundAmount  | Valor          |
| dueDate            | String        | Data de liquidação |
| error              | NuRefundError | Erro (se houver) |

---

## Notificações (IPN)

### NuNotificationStatus

Payload de webhook para mudança de status do pagamento.

| Campo           | Tipo   |
|-----------------|--------|
| pspReferenceId  | String |
| referenceId     | String |
| timestamp       | String |
| paymentMethodType | Type |

### NuNotificationRefundStatus

Payload de webhook para mudança de status do estorno.

| Campo             | Tipo   |
|-------------------|--------|
| pspReferenceId    | String |
| referenceId       | String |
| refundId          | String |
| transactionRefundId | String |
| timestamp         | String |
