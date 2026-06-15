package com.resume.paymentsystem.Mappers;

import com.resume.paymentsystem.DAO.Entity.Payment;
import com.resume.paymentsystem.DTO.PaymentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    Payment paymentDTOToPayment(PaymentDTO paymentDTO);

    PaymentDTO paymentToPaymentDTO(Payment payment);

    List<PaymentDTO> paymentsToPaymentDTO(List<Payment> payments);

    List<Payment> paymentDTOsToPayments(List<PaymentDTO> paymentDTOs);

}
