package com.resume.paymentsystem.Service;


import com.resume.paymentsystem.DTO.OrderRequest;
import com.stripe.model.PaymentIntent;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

public interface IPaymentService<T> {

 public Object createPayment(OrderRequest orderRequest) throws Exception;
 public String createSessionLink(T obj, HttpSession session) throws Exception;
 public Map<T,T> webhookEvent(String payload,String sigHeader) throws Exception;


}
