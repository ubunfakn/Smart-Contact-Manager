package com.mycompany.contactmanager.mycontactmanager.daorepository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.mycompany.contactmanager.mycontactmanager.entities.User;

public interface UserDao extends JpaRepository<User, Integer>{

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User findByEmail(@Param("email") String email);

}
