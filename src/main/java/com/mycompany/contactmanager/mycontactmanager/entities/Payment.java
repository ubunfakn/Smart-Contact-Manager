package com.mycompany.contactmanager.mycontactmanager.entities;

import java.util.Date;

import jakarta.persistence.*;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int amountPaid;
    private Date createdAt;
    private String orderId;
    private String reciept;
    private String status;
    @ManyToOne
    private User user;
    private String payment_id;

    public Payment() {
    }

    public Payment(long id, int amountPaid, Date createdAt, String order_id, String reciept, String status, User user,
            String payment_id) {
        this.id = id;
        this.amountPaid = amountPaid;
        this.createdAt = createdAt;
        this.orderId = order_id;
        this.reciept = reciept;
        this.status = status;
        this.user = user;
        this.payment_id = payment_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrder_id() {
        return orderId;
    }

    public void setOrder_id(String order_id) {
        this.orderId = order_id;
    }

    public String getReciept() {
        return reciept;
    }

    public void setReciept(String reciept) {
        this.reciept = reciept;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    @Override
    public String toString() {
        return "Payment [id=" + id + ", amountPaid=" + amountPaid + ", createdAt=" + createdAt + ", order_id="
                + orderId + ", reciept=" + reciept + ", status=" + status + ", user=" + user + ", payment_id="
                + payment_id + "]";
    }

}
