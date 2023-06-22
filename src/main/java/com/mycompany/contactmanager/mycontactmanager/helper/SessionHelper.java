package com.mycompany.contactmanager.mycontactmanager.helper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.*;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {
    
    public void removeAttributeFromSession()
    {
        try {
           HttpSession session= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
           session.removeAttribute("msg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
