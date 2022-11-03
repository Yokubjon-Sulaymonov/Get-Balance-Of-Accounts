package com.willhill.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.willhill.transcript.BalanceResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiRequest {

    private final String LINK;
    private HttpRequest request;
    private HttpResponse<String> response;

    private static Double sumOfBalance = 0.0;

    public ApiRequest createRequestFor(String accountNumber) {
        URI uri = URI.create(String.format(LINK, accountNumber));

        request = HttpRequest.newBuilder()
                             .uri(uri)
                             .build();
        return this;
    }

    public ApiRequest getResponse() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return this;
    }

    public Double getBalanceField() {
        Gson gson = new Gson();
        BalanceResponse balanceResponse = gson.fromJson(response.body(), BalanceResponse.class);

        if (balanceResponse.getBonusBalances() == null) {
            return 0.0;
        }

        double balance = balanceResponse.getPlayableBalance() - balanceResponse.getAccountBalance();
        sumOfBalance += balance;
        return balance;
    }

    public static Double getSumOfBalance() {
        return sumOfBalance;
    }
}
