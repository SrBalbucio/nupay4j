package balbucio.com.nubank.manager;

import balbucio.com.nubank.NuPay;
import balbucio.com.nubank.model.invoice.NuSummaryInvoice;
import balbucio.com.nubank.model.ipn.NuNotificationRefundStatus;
import balbucio.com.nubank.model.ipn.NuNotificationStatus;
import balbucio.com.nubank.utils.NuRequester;

import java.util.Optional;

public class NuNotificationManager {

    private final NuPay instance;
    private final NuRequester requester;

    public NuNotificationManager(NuPay instance, NuRequester requester) {
        this.instance = instance;
        this.requester = requester;
    }

    public NuNotificationStatus parseNotificationStatus(String body) {
        return requester.getGson().fromJson(body, NuNotificationStatus.class);
    }

    public NuNotificationRefundStatus parseNotificationRefundStatus(String body) {
        return requester.getGson().fromJson(body, NuNotificationRefundStatus.class);
    }

    public Optional<NuSummaryInvoice> getInvoiceFromNotificationStatus(NuNotificationStatus request) {
        return instance.getInvoice(request.getPspReferenceId());
    }

    public Optional<NuSummaryInvoice> getInvoiceFromNotificationRefundStatus(NuNotificationRefundStatus request) {
        return instance.getInvoice(request.getPspReferenceId());
    }

    public Optional<NuSummaryInvoice> getInvoiceFromNotificationStatus(String body) {
        NuNotificationStatus request = parseNotificationStatus(body);
        return instance.getInvoice(request.getPspReferenceId());
    }

    public Optional<NuSummaryInvoice> getInvoiceFromNotificationRefundStatus(String body) {
        NuNotificationRefundStatus request = parseNotificationRefundStatus(body);
        return instance.getInvoice(request.getPspReferenceId());
    }
}
