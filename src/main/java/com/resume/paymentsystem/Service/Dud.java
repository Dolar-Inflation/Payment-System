package com.resume.paymentsystem.Service;

import com.resume.paymentsystem.DTO.InvoiceDTO;
import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.deelter.yookassa.YooKassa;
import ru.deelter.yookassa.data.impl.Amount;
import ru.deelter.yookassa.data.impl.Currency;
import ru.deelter.yookassa.data.impl.Payment;
import ru.deelter.yookassa.data.impl.requests.PaymentCreateData;
import ru.deelter.yookassa.requests.payments.PaymentCaptureRequest;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Service
public class Dud {

//    @Value("${UCASSA_API_SECRET_KEY}")
//    private String ucassaApiSecretKey;

    private static final YooKassa YOO_KASSA = YooKassa.create(
            1388972, "test__k65ZNU2IPJpETbUZYWJt3sI_XhF2C5nAvVGVUxGRF8"

    );
    private final OkHttpClient httpClient;
    private static final String BASE_URL = "https://api.yookassa.ru/v3";
    private static final String INVOICES_ENDPOINT = "/invoices";
    private final ObjectMapper objectMapper;

    public Dud(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }


//         private static YooKassa YOO_KASSA = YooKassa.create(
//                1388972, "test__k65ZNU2IPJpETbUZYWJt3sI_XhF2C5nAvVGVUxGRF8"
//
//        );

    //    @PostConstruct
//    public void init() {
//        final YooKassa YOO_KASSA = YooKassa.create(
//                1388972,ucassaApiSecretKey
//
//        );
//    }
    public ResponseEntity<?> paymentCaptureRequest(UUID id) throws IOException {
        try {
            YOO_KASSA.capturePayment(id, Amount.from(100, Currency.RUB));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    public String CreateInvoice(InvoiceDTO invoiceDTO) throws IOException {

        ObjectNode root = objectMapper.createObjectNode();


        ObjectNode paymentData = root.putObject("payment_data");
        ObjectNode amountNode = paymentData.putObject("amount");
        amountNode.put("value", invoiceDTO.getAmount());
        amountNode.put("currency", invoiceDTO.getCurrency());

        paymentData.put("capture", true);
        paymentData.put("description", invoiceDTO.getDescription());

        root.set("cart", objectMapper.valueToTree(invoiceDTO.getCart()));

        root.put("description", invoiceDTO.getDescription());
        root.put("locale", invoiceDTO.getLocale() != null ? invoiceDTO.getLocale() : "ru_RU");
        if (invoiceDTO.getExpiresAt() != null) {
            root.put("expires_at", invoiceDTO.getExpiresAt());
        }

        String auth = "Basic " + Base64.getEncoder().encodeToString(
                ("1388972" + ":" + "test__k65ZNU2IPJpETbUZYWJt3sI_XhF2C5nAvVGVUxGRF8").getBytes(StandardCharsets.UTF_8)
        );

        String jsonBody = objectMapper.writeValueAsString(root);

        String idempotenceKey = UUID.randomUUID().toString();
        Request request = new Request.Builder()
                .url(BASE_URL + INVOICES_ENDPOINT)
                .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                .header("Idempotence-Key", idempotenceKey)
                .header("Authorization", auth)
                .build();


        System.out.println("Sending JSON: " + jsonBody);
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                throw new IOException("Ошибка при создании счёта. Код: " + response.code()
                        + ", тело: " + errorBody);
            }

            String responseBody = response.body().string();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);


            JsonNode confirmationNode = jsonResponse.get("confirmation");
            if (confirmationNode != null && confirmationNode.has("confirmation_url")) {
                return confirmationNode.get("confirmation_url").asText();

            }
            else {
                JsonNode paymentUrl = jsonResponse.get("payment_url");

                if (paymentUrl != null) {
                    return paymentUrl.asText();
                }
                throw new IOException("В ответе отсутствует ссылка на оплату");
            }
        }
    }
}

//@JsonDeserialize
//    public Payment createPayment() throws IOException {
//        return YOO_KASSA.createPayment(PaymentCreateData.builder()
//                .amount(Amount.from(100, Currency.RUB))
//                .description("Buy a coffee")
//                .redirect("https://github.com/deelter")
//                .capture(true)
//                .build()
//        );
//    }
//    public Payment getPayment(UUID paymentId) throws IOException {
//        return YOO_KASSA.getPayment(paymentId);
//    }
//
//    // Is payment status success
//    public boolean isSuccess(UUID paymentId) throws IOException {
//        return getPayment(paymentId).getStatus().isSuccess();
//    }
//


