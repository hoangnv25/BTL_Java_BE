package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Orders.Order;
import com.BTL_JAVA.BTL.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // Tìm payment bằng VNPay transaction ref
    Optional<Payment> findByVnpayTransactionRef(String vnpayTransactionRef);

    // Tìm payment bằng order
    Optional<Payment> findByOrder(Order order);

    // Tìm tất cả payments của user
    List<Payment> findByOrder_User_Id(Integer userId);
}
