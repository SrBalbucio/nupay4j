package balbucio.com.nubank.utils;

import balbucio.com.nubank.NuPay;
import balbucio.com.nubank.exception.NuInternalError;
import balbucio.com.nubank.model.config.NuPayConfig;
import balbucio.com.nubank.model.response.NuResponseError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class NuRequester {

    public static String NUPAY_SANDBOX_ENDPOINT = "https://sandbox-api.spinpay.com.br/";
    public static String NUPAY_PRODUCTION_ENDPOINT = "https://api.spinpay.com.br/";

    private final NuPay instance;
    private final NuPayConfig config;
    @Getter
    private final Gson gson;

    public NuRequester(NuPay instance, NuPayConfig config) {
        this.instance = instance;
        this.config = config;
        this.gson = new GsonBuilder().enableComplexMapKeySerialization().create();
    }

    public String getUrl(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        return config.isSandboxMode() ? NUPAY_SANDBOX_ENDPOINT + path : NUPAY_PRODUCTION_ENDPOINT + path;
    }

    public Connection getConnection(String path, Connection.Method method, String body) {
        Connection connection = Jsoup.connect(getUrl(path))
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("X-Merchant-Key", config.getCredential().getApiKey())
                .header("X-Merchant-Token", config.getCredential().getApiToken())
                .method(method);

        if ((method == Connection.Method.POST || method == Connection.Method.PUT || method == Connection.Method.PATCH) && body != null) {
            connection.requestBody(body);
        }

        return connection;
    }

    public Connection.Response post(String path, JSONObject body) throws NuInternalError {
        return post(path, body.toString());
    }

    public Connection.Response post(String path, Object body) throws NuInternalError {
        return post(path, gson.toJson(body));
    }

    public Connection.Response post(String path, String body) throws NuInternalError {
        try {
            return getConnection(path, Connection.Method.POST, body).execute();
        } catch (Exception e) {
            throw new NuInternalError(e.getMessage());
        }
    }

    public Connection.Response get(String path) throws NuInternalError {
        try {
            return getConnection(path, Connection.Method.GET, null).execute();
        } catch (Exception e) {
            throw new NuInternalError(e.getMessage());
        }
    }

    public NuResponseError getResponseError(Connection.Response response) {
        return gson.fromJson(response.body(), NuResponseError.class);
    }
}
