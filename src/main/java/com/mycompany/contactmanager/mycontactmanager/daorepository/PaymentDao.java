package com.mycompany.contactmanager.mycontactmanager.daorepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.contactmanager.mycontactmanager.entities.Payment;

public interface PaymentDao extends JpaRepository<Payment, Integer> {

    public Payment findByOrderId(String orderId);
    
}
