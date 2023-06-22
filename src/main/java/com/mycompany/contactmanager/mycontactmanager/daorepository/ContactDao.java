package com.mycompany.contactmanager.mycontactmanager.daorepository;


import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

import com.mycompany.contactmanager.mycontactmanager.entities.Contact;
import com.mycompany.contactmanager.mycontactmanager.entities.User;

public interface ContactDao extends JpaRepository<Contact, Integer> {
    
    @Query("from Contact as c where c.user.id =:userId")
    //Pageable stores information about current page and size of each page
    public Page<Contact> findContactByYser(@Param("userId") int userId, Pageable pageable);


    public List<Contact> findByNameContainingAndUser(String keyword, User user);
}
