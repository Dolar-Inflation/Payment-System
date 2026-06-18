package com.resume.paymentsystem.Service;

import com.resume.paymentsystem.DTO.OrderRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.deelter.yookassa.YooKassa;

import java.util.Map;

@Service
public class YookassStripePaymentService {

    @Value("${UCASSA_API_SECRET_KEY}")
    private String ucassaApiSecretKey;

//    private final YooKassa yooKassa = YooKassa.create(1388972, ucassaApiSecretKey);



}
