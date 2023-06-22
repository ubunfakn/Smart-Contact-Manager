package com.mycompany.contactmanager.mycontactmanager.config;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mycompany.contactmanager.mycontactmanager.entities.User;

public class CustomUserDetail implements UserDetails{

    private User user1;
    

    public CustomUserDetail(User user) {
        this.user1=user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        SimpleGrantedAuthority simpleGrantedAuthority=null;
        if(this.user1!=null){
        simpleGrantedAuthority=new SimpleGrantedAuthority(user1.getRole());
        }
        return List.of(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        if(this.user1==null)
        {
            return "Invalid Username or Paswword";
        }
        else 
        return user1.getPassword();
    }

    @Override
    public String getUsername() {

        if(this.user1==null)
        {
            return "Invalid Username or Paswword";
        }
        else 
        return user1.getEmail();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
