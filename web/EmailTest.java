/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.WEB;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailTest {
    public static void main(String[] args) {
        final String from = "nadiyatulh18@gmail.com";
        final String password = "rgkuepjteafjwcij"; // App password
        final String to = "nadiyatulhusna14@gmail.com"; // ✅ Recipient

        Properties props = new Properties();
props.put("mail.smtp.host", "smtp.gmail.com");
props.put("mail.smtp.port", "465");
props.put("mail.smtp.auth", "true");
props.put("mail.smtp.socketFactory.port", "465");
props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
props.put("mail.smtp.socketFactory.fallback", "false");  // ✅ recommended


// 🔧 Add timeout settings (in milliseconds)
props.put("mail.smtp.connectiontimeout", "10000"); // 10 seconds
props.put("mail.smtp.timeout", "10000");
props.put("mail.smtp.writetimeout", "10000");


        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
        return new javax.mail.PasswordAuthentication(from, password);
    }
});
session.setDebug(true);


        System.out.println("Using email: " + from);
        System.out.println("Using app password: " + password);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Test Email");
            message.setText("This is a test email sent using JavaMail.");

            Transport.send(message);
            System.out.println("✅ Email sent.");
        } catch (MessagingException e) {
            System.out.println("❌ Email failed.");
            e.printStackTrace();
        }
    }
}
