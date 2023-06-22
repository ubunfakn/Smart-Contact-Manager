package com.mycompany.contactmanager.mycontactmanager.emailservice;

import java.io.File;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import org.springframework.stereotype.Service;

@Service
public class SendEmail {

    public static void sendEmail(String message, String subject, String to, String from) throws MessagingException {
        // G-Mail host
        String host = "smtp.googlemail.com";

        // Get the system properties
        Properties properties = System.getProperties();

        // setting important information to properties object
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // To get the session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ankitnashine12@gmail.com", "liywpprgdugiybxx");
            }
        });

        session.setDebug(true);

        // Compose the message[text,multimedia]
        MimeMessage mimeMessage = new MimeMessage(session);

        // From E-mail
        mimeMessage.setFrom(from);

        // adding recipient to message
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        // adding subject to message
        mimeMessage.setSubject(subject);

        // adding text to message
        mimeMessage.setText(message);

        // send message
        Transport.send(mimeMessage);
        System.out.println("Message sent Successfully");

    }

    public static void sendAttach(String message, String subject, String to, String from) throws MessagingException{
        // G-Mail host
        String host = "smtp.googlemail.com";

        // Get the system properties
        Properties properties = System.getProperties();

        // setting important information to properties object
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // To get the session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ankitnashine12@gmail.com", "liywpprgdugiybxx");
            }
        });

        session.setDebug(true);

        // Compose the message[text,multimedia]
        MimeMessage mimeMessage = new MimeMessage(session);

        // From E-mail
        mimeMessage.setFrom(from);

        // adding recipient to message
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        // adding subject to message
        mimeMessage.setSubject(subject);

        //attachment

        //file path
        String path = "/home/akn/Documents/JAVA/Spring_Boot/RESTAPI/SmartContactManager/mycontactmanager/src/main/resources/static/Images/background.jpg";
        Multipart multipart = new MimeMultipart();
        //text and file
        
        MimeBodyPart textMime = new MimeBodyPart();
        MimeBodyPart fileMime = new MimeBodyPart();

        mimeMessage.setContent(multipart);

        try {
            
            textMime.setText(message);
            File file = new File(path);
            fileMime.attachFile(file);

            multipart.addBodyPart(textMime);
            multipart.addBodyPart(fileMime);


        } catch (Exception e) {

            e.printStackTrace();
        }

        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);
    }
}
