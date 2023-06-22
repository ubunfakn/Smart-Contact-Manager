package com.mycompany.contactmanager.mycontactmanager.controll;

import java.io.File;
import java.nio.file.*;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.mycompany.contactmanager.mycontactmanager.daorepository.*;
import com.mycompany.contactmanager.mycontactmanager.entities.*;
import com.mycompany.contactmanager.mycontactmanager.entities.Payment;
import com.mycompany.contactmanager.mycontactmanager.helper.Message;
import com.razorpay.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserDao userDao;

    @Autowired
    ContactDao contactDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PaymentDao paymentDao;

    @ModelAttribute
    public void commonContent(Model model, Principal principal) {
        model.addAttribute("user", userDao.findByEmail(principal.getName()));
    }

    @GetMapping("/home")
    public String homeHandler() {
        return "normal/home";
    }

    // Dashboard
    @RequestMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "User Home");
        return "normal/user_dashboard";
    }

    @GetMapping("/addcontact")
    public String addFormHandler(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/addContactForm";
    }

    @PostMapping("/processcontact")
    public String contactFormHandler(@ModelAttribute Contact contact,
            @RequestParam("profileimage") MultipartFile file,
            Principal principal,
            Model model,
            HttpSession session) {
        try {

            String email = principal.getName();
            User user = userDao.findByEmail(email);

            // Processing and uploading file
            if (file.isEmpty()) {
                System.out.println("File is empty");
                contact.setImageURL("default.png");
            } else {
                contact.setImageURL("_" + user.getId() +
                        LocalDate.now() + file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/Images").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator +
                        "_" + user.getId() + LocalDate.now() + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            contact.setUser(user);
            user.getContacts().add(contact);
            this.userDao.save(user);

            session.setAttribute("msg", new Message("Contact successfully added ", "success"));

        } catch (Exception e) {
            session.setAttribute("msg",
                    new Message("Something went wrong !! Try again with proper Credentials", "danger"));
            e.printStackTrace();
        }
        return "normal/addContactForm";
    }

    @GetMapping("/showcontact/{page}")
    public String showContactHandler(@PathVariable("page") int page, Model m, Principal principal) {
        String email = principal.getName();
        m.addAttribute("title", "Show Contact");

        User user = userDao.findByEmail(email);

        // It holds information about current page and size of each page
        Pageable pageable = PageRequest.of(page, 5);
        Page<Contact> contacList = this.contactDao.findContactByYser(user.getId(), pageable);

        m.addAttribute("contacts", contacList);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalpage", contacList.getTotalPages());

        System.out.println();

        return "normal/show_contact";
    }

    @RequestMapping("/contact/{cid}")
    public String contactHandler(@PathVariable("cid") int cid, Model m, Principal principal) {
        Optional<Contact> contactOptional = this.contactDao.findById(cid);
        Contact contact = contactOptional.get();
        String email = principal.getName();
        User user = userDao.findByEmail(email);
        if (user.getId() == contact.getUser().getId())
            m.addAttribute("contact", contact);

        m.addAttribute("title", "Contact-Details");
        return "normal/contact_detail";
    }

    @RequestMapping("/delcont/{cid}")
    public RedirectView deleteContact(@PathVariable("cid") int cid, Principal principal) {
        Optional<Contact> contactOptional = this.contactDao.findById(cid);
        Contact contact = contactOptional.get();

        String email = principal.getName();
        User user = userDao.findByEmail(email);
        if (user.getId() == contact.getUser().getId())
            this.contactDao.delete(contact);

        RedirectView redirectView = new RedirectView("/user/showcontact/0");

        return redirectView;
    }

    @RequestMapping("/upcont/{cid}")
    public String updateContactHandler(@PathVariable("cid") int cid, Model m, Principal principal) {
        Optional<Contact> contactOptional = this.contactDao.findById(cid);
        Contact contact = contactOptional.get();

        String email = principal.getName();
        User user = userDao.findByEmail(email);
        if (user.getId() == contact.getUser().getId())
            m.addAttribute("contact", contact);
        m.addAttribute("title", "Update-Contact");

        return "normal/update_Contact";
    }

    @PostMapping("/updatecontact/{cid}")
    public RedirectView updateContact(
                @ModelAttribute Contact contact1, 
                @PathVariable("cid")int cid, 
                Principal principal, 
                HttpSession session)
    {
        RedirectView redirectView=null;
        try{
            User user=this.userDao.findByEmail(principal.getName());
            contact1.setUser(user);
        
            contact1.setImageURL(this.contactDao.findById(contact1.getCid()).get().getImageURL());
            this.contactDao.save(contact1);

            session.setAttribute("msg", new Message("Contact Updated Successfully", "success"));
            redirectView=new RedirectView("/user/showcontact/0");

        }catch(Exception e)
        {
            session.setAttribute("msg", new Message("Something wen Wrong! Contact not updated!! Please Try again", "danger"));
            int cid1=contact1.getCid();
            System.out.println(cid1);
            return new RedirectView("/user/showcontact/0");
        }

        
        return redirectView;
    }

    @GetMapping("/profile")
    public String yourProfile(Model model)
    {
        model.addAttribute("title", "Profile");
        return "normal/user_Profile";
    }

    @GetMapping("/profile/edit")
    public String editProfileHandler(Model model)
    {
        model.addAttribute("title", "Edit-Profile");
        return "normal/edit_Profile";
    }

    @PostMapping("/profile/doedit")
    public String editProfile(@ModelAttribute User user1, Principal principal)
    {
        user1.setImageURL(this.userDao.findByEmail(principal.getName()).getImageURL());
        user1.setEnabled(true);
        user1.setRole("ROLE_USER");

        this.userDao.save(user1);
        return "normal/user_Profile";
    }

    @GetMapping("/settings")
    public String openSettings(Model model)
    {
        model.addAttribute("title", "Settings");
        return "normal/settings";
    }

    @PostMapping("/changepassword")
    public String changePassword(@RequestParam("old-password")String oldPassword, @RequestParam("new-password")String newPassword, @RequestParam("re-new-password")String reEnterPassword, HttpSession session, Principal principal)
    {
        if(!newPassword.equals(reEnterPassword))
        {
            session.setAttribute("msg", new Message("Re-Entered Password mismatched", "danger"));
            return "normal/settings";
        }
        if(newPassword.equals(oldPassword))
        {
            session.setAttribute("msg", new Message("Old Password and New Password must be different", "danger"));
            return "normal/settings";
        }
        else{
            User user=this.userDao.findByEmail(principal.getName());
            if(passwordEncoder.matches(oldPassword, user.getPassword()))
            {
                user.setPassword(passwordEncoder.encode(newPassword));
                this.userDao.save(user);
                session.setAttribute("msg", new Message("Password Changed Successfully", "success"));
                return "redirect:/user/settings";
            }
            else
            {
                session.setAttribute("msg", new Message("In-Correct Old Password", "danger"));
                return "redirect:/user/settings";
            }
        }
    }
    //Creating order for payment
    @PostMapping("/create_order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> data, Principal principal)throws RazorpayException
    {
        System.out.println("order function executed");
        System.out.println(data);
        int amount = Integer.parseInt(data.get("amount").toString());

        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_PFH56BsCj9Z0Ty", "IJKS8EJtve9OsyFrtOfuUR8D");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", amount*100);
        jsonObject.put("currency", "INR");
        jsonObject.put("receipt", "order_2486");

        //creating new order
        Order order = razorpayClient.orders.create(jsonObject);
        System.out.println(order);

        //if we want we can save this order info to our personal database
        Payment payment=new Payment();
        payment.setAmountPaid((Integer)(order.get("amount"))/100);
        payment.setOrder_id(order.get("id"));
        payment.setCreatedAt(order.get("created_at"));
        payment.setPayment_id(null);
        payment.setReciept(order.get("receipt"));
        payment.setUser(this.userDao.findByEmail(principal.getName()));
        payment.setStatus("created");

        this.paymentDao.save(payment);

        return order.toString();
    }

    @PostMapping("/update_order")
    public ResponseEntity<?> updateStatus(@RequestBody Map<String, Object> data)
    {
        System.out.println(data);
        Payment payment = this.paymentDao.findByOrderId(data.get("order_id").toString());
        payment.setPayment_id(data.get("payment_id").toString());
        payment.setStatus(data.get("statuses").toString());
        this.paymentDao.save(payment);

        return ResponseEntity.ok(Map.of("msg", "updated"));
    }
}
