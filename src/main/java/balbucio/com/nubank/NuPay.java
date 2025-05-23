package balbucio.com.nubank;

import balbucio.com.nubank.exception.NuException;
import balbucio.com.nubank.exception.NuInternalError;
import balbucio.com.nubank.exception.NuRequestException;
import balbucio.com.nubank.manager.NuNotificationManager;
import balbucio.com.nubank.model.cancel.NuCancelResponse;
import balbucio.com.nubank.model.config.NuPayConfig;
import balbucio.com.nubank.model.invoice.NuInvoice;
import balbucio.com.nubank.model.invoice.NuPaymentMethod;
import balbucio.com.nubank.model.invoice.NuSummaryInvoice;
import balbucio.com.nubank.model.refund.NuRefund;
import balbucio.com.nubank.model.refund.NuRefundRequest;
import balbucio.com.nubank.model.response.NuPayCheckoutResponse;
import balbucio.com.nubank.model.response.NuResponseError;
import balbucio.com.nubank.utils.NuRequester;
import lombok.Getter;
import org.json.JSONObject;
import org.jsoup.Connection;

import java.util.Optional;
import java.util.concurrent.*;

public class NuPay {

    @Getter
    private final NuPayConfig config;
    private final NuRequester requester;
    private final Executor executor;
    @Getter
    private final NuNotificationManager notificationManager;

    public NuPay(NuPayConfig config) {
        this(config, Executors.newCachedThreadPool());
    }

    public NuPay(NuPayConfig config, Executor executor) {
        this.config = config;
        this.requester = new NuRequester(this, config);
        this.executor = executor;
        this.notificationManager = new NuNotificationManager(this, requester);
    }

    public NuPayCheckoutResponse createPayment(NuInvoice invoice) throws NuException {
        // modificações para caso não tenha sido configurado corretamente
        invoice.getPaymentMethod().setAuthorizationType(NuPaymentMethod.AuthorizationType.MANUALLY);

        Connection.Response response = requester.post("v1/checkouts/payments", invoice);

        if (response.statusCode() != 200) {
            NuResponseError error = requester.getResponseError(response);
            throw new NuRequestException(error);
        }

        return requester.getGson().fromJson(response.body(), NuPayCheckoutResponse.class);
    }

    public NuPayCheckoutResponse createPreAuthorizedPayment(NuInvoice invoice, String accessToken) throws NuException {
        if (accessToken == null || accessToken.isEmpty() || config.getCredential().getClientId() == null)
            throw new NuInternalError("No valid credentials for this payment type.");

        // modificações para caso não tenha sido configurado corretamente
        invoice.getPaymentMethod().setAuthorizationType(NuPaymentMethod.AuthorizationType.PRE_AUTHORIZED);

        Connection connection = requester.getConnection("v1/checkouts/payments", Connection.Method.POST, requester.getGson().toJson(invoice));

        connection.header("Authorization", "Bearer " + accessToken);
        Connection.Response response;

        try {
            response = connection.execute();
        } catch (Exception e) {
            throw new NuInternalError(e.getMessage());
        }

        if (response.statusCode() != 200) {
            NuResponseError error = requester.getResponseError(response);
            throw new NuRequestException(error);
        }

        return requester.getGson().fromJson(response.body(), NuPayCheckoutResponse.class);
    }

    public Future<NuPayCheckoutResponse> createAsyncPayment(NuInvoice invoice) {
        Future<NuPayCheckoutResponse> future = new CompletableFuture<>();

        executor.execute(() -> {
            try {
                ((CompletableFuture) future).complete(createPayment(invoice));
            } catch (Exception e) {
                ((CompletableFuture) future).completeExceptionally(e);
            }
        });

        return future;
    }

    public Future<NuPayCheckoutResponse> createAsyncPreAuthorizedPayment(NuInvoice invoice) {
        Future<NuPayCheckoutResponse> future = new CompletableFuture<>();

        executor.execute(() -> {
            try {
                ((CompletableFuture) future).complete(createAsyncPreAuthorizedPayment(invoice));
            } catch (Exception e) {
                ((CompletableFuture) future).completeExceptionally(e);
            }
        });

        return future;
    }

    public Optional<NuSummaryInvoice> getInvoice(String pspReferenceId) throws NuException {
        Connection.Response response = requester.get("v1/checkouts/" + pspReferenceId + "/status");
        if (response.statusCode() == 404) return Optional.empty();
        if (response.statusCode() != 200) {
            NuResponseError error = requester.getResponseError(response);
            throw new NuRequestException(error);
        }

        return Optional.ofNullable(requester.getGson().fromJson(response.body(), NuSummaryInvoice.class));
    }

    public Future<Optional<NuSummaryInvoice>> getAsyncInvoice(String pspReferenceId) {
        Future<Optional<NuSummaryInvoice>> future = new CompletableFuture<>();

        executor.execute(() -> {
            try {
                ((CompletableFuture) future).complete(getInvoice(pspReferenceId));
            } catch (Exception e) {
                ((CompletableFuture) future).completeExceptionally(e);
            }
        });
        return future;
    }

    public Optional<NuCancelResponse> cancelPayment(String pspReferenceId) throws NuException {
        Connection.Response response = requester.post("v1/checkouts/" + pspReferenceId + "/cancel", (JSONObject) null);

        if (response.statusCode() == 404) return Optional.empty();
        if (response.statusCode() != 200) {
            NuResponseError error = requester.getResponseError(response);
            throw new NuRequestException(error);
        }

        return Optional.ofNullable(requester.getGson().fromJson(response.body(), NuCancelResponse.class));
    }

    public Future<Optional<NuCancelResponse>> asyncCancelPayment(String pspReferenceId) {
        Future<Optional<NuCancelResponse>> future = new CompletableFuture<>();

        executor.execute(() -> {
            try {
                ((CompletableFuture) future).complete(cancelPayment(pspReferenceId));
            } catch (Exception e) {
                ((CompletableFuture) future).completeExceptionally(e);
            }
        });
        return future;
    }

    public Optional<NuRefund> createRefund(String pspReferenceId, NuRefundRequest request) throws NuException {
        Connection.Response response = requester.post("v1/checkouts/" + pspReferenceId + "/refunds", request);

        if (response.statusCode() == 404) return Optional.empty();
        if (response.statusCode() != 200) {
            NuResponseError error = requester.getResponseError(response);
            throw new NuRequestException(error);
        }

        return Optional.ofNullable(requester.getGson().fromJson(response.body(), NuRefund.class));
    }

    public Future<Optional<NuRefund>> createAsyncRefund(String pspReferenceId, NuRefundRequest request) {
        Future<Optional<NuRefund>> future = new CompletableFuture<>();

        executor.execute(() -> {
            try {
                ((CompletableFuture) future).complete(createRefund(pspReferenceId, request));
            } catch (Exception e) {
                ((CompletableFuture) future).completeExceptionally(e);
            }
        });
        return future;
    }

    public Optional<NuRefund> getRefund(String pspReferenceId, String refundId) throws NuException {
        Connection.Response response = requester.get("v1/checkouts/" + pspReferenceId + "/refunds/"+refundId);

        if (response.statusCode() == 404) return Optional.empty();
        if (response.statusCode() != 200) {
            NuResponseError error = requester.getResponseError(response);
            throw new NuRequestException(error);
        }

        return Optional.ofNullable(requester.getGson().fromJson(response.body(), NuRefund.class));
    }

    public Future<Optional<NuRefund>> getAsyncRefund(String pspReferenceId, String refundId) {
        Future<Optional<NuRefund>> future = new CompletableFuture<>();

        executor.execute(() -> {
            try {
                ((CompletableFuture) future).complete(getRefund(pspReferenceId, refundId));
            } catch (Exception e) {
                ((CompletableFuture) future).completeExceptionally(e);
            }
        });
        return future;
    }
}
