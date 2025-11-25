package com.BTL_JAVA.BTL.Service.Payment;

import com.BTL_JAVA.BTL.Entity.Orders.Order;
import com.BTL_JAVA.BTL.Entity.Payment;
import com.BTL_JAVA.BTL.Repository.OrderRepository;
import com.BTL_JAVA.BTL.Repository.PaymentRepository;
import com.BTL_JAVA.BTL.enums.OrderStatus;
import com.BTL_JAVA.BTL.enums.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentTimeoutService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Scheduled(fixedRate = 60000) // 1 phút chạy 1 lần
    public void checkExpiredPayments() {

        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);

        List<Payment> pendingExpired = paymentRepository
                .findByStatusAndPaymentMethodAndCreatedDateBefore(
                        PaymentStatus.PENDING,
                        "VNPAY",
                        fifteenMinutesAgo
                );
        List<Payment> failedPayments = paymentRepository
                .findByStatusAndPaymentMethod(
                        PaymentStatus.FAILED
                );

        List<Payment> expiredPayments = new ArrayList<>();
        expiredPayments.addAll(pendingExpired);
        expiredPayments.addAll(failedPayments);

        for (Payment payment : expiredPayments) {
            Order order = payment.getOrder();
            order.setStatus(OrderStatus.CANCELED);
            payment.setStatus(PaymentStatus.FAILED);

            paymentRepository.save(payment);
            orderRepository.save(order);
        }
    }
}

