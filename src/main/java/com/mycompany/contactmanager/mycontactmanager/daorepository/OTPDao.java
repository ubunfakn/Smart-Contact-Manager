package com.mycompany.contactmanager.mycontactmanager.daorepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.contactmanager.mycontactmanager.entities.OTP;

public interface OTPDao extends JpaRepository<OTP,Integer>{
    
}
