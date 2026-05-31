//package com.resume.paymentsystem.Service;
//
//import com.resume.paymentsystem.DAO.Entity.Payment;
//import com.resume.paymentsystem.DTO.OrderRequest;
//import com.resume.paymentsystem.DTO.PaymentResponse;
//import com.resume.paymentsystem.DTO.PaymentSummary;
//
//import java.util.List;
//
//public interface IPaymentService {
//
//    PaymentResponse createPayment(OrderRequest orderRequest,String idempotencyKey);
//    String createCheckoutSession(OrderRequest orderRequest,String idempotencyKey,String baseUrl);
//
//    String handleWebhook(String signatureHead, String payload);
//    PaymentResponse getPaymentStatus(String uuId);
//    PaymentResponse getPaymentStatus(Long id);
//
//    List<PaymentSummary> listAllPayments();
//
//
//}
