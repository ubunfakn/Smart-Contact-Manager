package com.mycompany.contactmanager.mycontactmanager.entities;

import jakarta.persistence.*;

@Entity
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int otp;
    private String email;

    public OTP(int otp, String email) {
        this.otp = otp;
        this.email = email;
    }

    public OTP() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public int getOtp() {
        return this.otp;
    }
}
