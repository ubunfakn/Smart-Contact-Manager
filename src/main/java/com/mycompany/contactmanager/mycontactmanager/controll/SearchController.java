package com.mycompany.contactmanager.mycontactmanager.controll;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mycompany.contactmanager.mycontactmanager.daorepository.*;
import com.mycompany.contactmanager.mycontactmanager.entities.*;
import java.util.*;;

@RestController
public class SearchController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ContactDao contactDao;
    
    @GetMapping("/search/{query}")
    public ResponseEntity<?> searchHandler(@PathVariable("query")String query ,Principal principal)
    {
        System.out.println(query);
        List<Contact> contacts = this.contactDao.findByNameContainingAndUser(query, this.userDao.findByEmail(principal.getName()));

        return ResponseEntity.ok(contacts);
    }
}
