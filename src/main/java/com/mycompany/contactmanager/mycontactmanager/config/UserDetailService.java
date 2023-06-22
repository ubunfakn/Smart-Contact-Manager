package com.mycompany.contactmanager.mycontactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import com.mycompany.contactmanager.mycontactmanager.daorepository.UserDao;
import com.mycompany.contactmanager.mycontactmanager.entities.User;
@Component
public class UserDetailService implements UserDetailsService{

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user= userDao.findByEmail(username);
        try
        {
            if(user==null) throw new UsernameNotFoundException("*******************************User not found******************");
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        UserDetails userDetails=new CustomUserDetail(user);
        return userDetails;
    }

}