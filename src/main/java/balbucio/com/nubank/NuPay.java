package balbucio.com.nubank;

import balbucio.com.nubank.exception.NuException;
import balbucio.com.nubank.exception.NuInternalError;
import balbucio.com.nubank.exception.NuRequestException;
import balbucio.com.nubank.model.config.NuPayConfig;
import balbucio.com.nubank.model.invoice.NuInvoice;
import balbucio.com.nubank.model.invoice.NuSummaryInvoice;
import balbucio.com.nubank.model.response.NuPayCheckoutResponse;
import balbucio.com.nubank.model.response.NuResponseError;
import balbucio.com.nubank.utils.NuRequester;
import org.jsoup.Connection;

import java.util.Optional;
import java.util.concurrent.*;

public class NuPay {

    private final NuPayConfig config;
    private final NuRequester requester;
    private final Executor executor;

    public NuPay(NuPayConfig config) {
        this(config, Executors.newCachedThreadPool());
    }

    public NuPay(NuPayConfig config, Executor executor) {
        this.config = config;
        this.requester = new NuRequester(this, config);
        this.executor = executor;
    }

    public NuPayCheckoutResponse createPayment(NuInvoice invoice) throws NuException {
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
        Connection.Response response = requester.get("/v1/checkouts/" + pspReferenceId + "/status");
        if (response.statusCode() == 404) return Optional.empty();
        if (response.statusCode() != 200) {
            NuResponseError error = requester.getResponseError(response);
            throw new NuRequestException(error);
        }

        return Optional.ofNullable(requester.getGson().fromJson(response.body(), NuSummaryInvoice.class));
    }

    public Future<Optional<NuSummaryInvoice>> getAsyncInvoice(String pspReferenceId){
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
}
