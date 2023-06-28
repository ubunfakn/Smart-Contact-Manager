package com.mycompany.contactmanager.mycontactmanager.controll;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.mycompany.contactmanager.mycontactmanager.daorepository.*;
import com.mycompany.contactmanager.mycontactmanager.emailservice.SendEmail;
import com.mycompany.contactmanager.mycontactmanager.entities.*;
import com.mycompany.contactmanager.mycontactmanager.helper.Message;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OTPDao otpDao;

    @GetMapping("/")
    public String home(Model m) {
        m.addAttribute("title", "Home      Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model m) {
        m.addAttribute("title", "About      Smart Contact Manager");
        return "about";
    }

    @GetMapping("/signup")
    public String signUp(Model m) {
        m.addAttribute("title", "Sign-Up      Smart Contact Manager");
        m.addAttribute("user", new User());
        return "signUp";
    }

    @PostMapping("/register")
    public String formHandler(@Valid @ModelAttribute("user") User user,BindingResult result ,@RequestParam(value = "agreement", defaultValue = "false") boolean agreement,Model model, HttpSession session) {
      

        try {
            if (!agreement) {
                throw new Exception("Please Agree to Terms and Conditions");
            }
            if(result.hasErrors())
            {
                model.addAttribute("user", user);
                System.out.println(result.toString());
                return "signUp";
            }
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageURL("default.png");
            System.out.println(user.getPassword());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println(user.getPassword());

            this.userDao.save(user);
            model.addAttribute("user", new User());
            session.setAttribute("msg", new Message("Successfully Registered", "success"));

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute(user);
            session.setAttribute("msg", new Message("!Something went wrong", "danger"));
        }

        return "signUp";
    }

    @GetMapping("/signin")
    public String customLogin(Model m)
    {
        m.addAttribute("title", "Sign-In      Smart Contact Manager");
        return "login";
    }

    @GetMapping("/forgotpassword")
    public String forgot(Model model)
    {
        model.addAttribute("title", "Forgot Password");
        return "forgot_password";
    }

    @PostMapping("/sendOTP")
    public String sendOTP(@RequestParam("email")String email, Model model, HttpSession session)
    {
        if(this.userDao.findByEmail(email)==null)
        {
            session.setAttribute("msg", new Message("Username is not registered with us!! Please use valid username", "danger"));
            return "redirect:/forgotpassword";
        }
        model.addAttribute("title", "OTP-Verification");
        System.out.println(email);

        
        int otp = (int) (Math.random() * 49998 + 1);
        System.out.println(otp);
        OTP oTP = new OTP();
        oTP.setOtp(otp);
        oTP.setEmail(email);

        //Sending otp
        try {

            otpDao.deleteAll();
            otpDao.save(oTP);

            SendEmail.sendEmail(String.valueOf(otp), "Change Password Verification OTP", email, "ankitnashine12@gmail.com");
            System.out.println("Email sent successfully");
            session.setAttribute("msg", new Message("OTP Sent to your registered E-Mail id", "success"));

        
        } catch (Exception e) {

            session.setAttribute("msg", new Message("We're having trouble reaching you", "danger"));
            e.printStackTrace();
        }

        return "verify_otp";
    }

    @PostMapping("/validateOTP")
    public String validatePassword(@RequestParam("otp")int otp, Model model, HttpSession session)
    {
        model.addAttribute("title", "Change-Password");
        List<OTP> list = otpDao.findAll();
        for(int i=0;i<list.size();i++)if(list.get(i).getOtp()==otp)return "change_password_form";
        session.setAttribute("msg", new Message("Please Enter correct OTP!", "danger"));
        return "verify_otp";
    }

    @PostMapping("/passwordchange")
    public String changePasswordHandler(@RequestParam("new-password")String newPassword, @RequestParam("re-new-password")String confirmedPassword, Model model, HttpSession session)
    {
        model.addAttribute("title", "Change-Password");
        if(newPassword.equals(confirmedPassword))
    
        {
           List<OTP> list= this.otpDao.findAll();
           String email=list.get(0).getEmail();
           User user=this.userDao.findByEmail(email);
           user.setPassword(passwordEncoder.encode(newPassword));
           this.userDao.save(user);
           return "redirect:/signin";
        }
        else
        {
            session.setAttribute("msg", new Message("Password Mismatched", "danger"));
            return "change_password_form";
        }
    }
}
