/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.DAO.staffDAO;
import com.Model.Staff;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SaveStaffServlet", urlPatterns = {"/SaveStaffServlet"})
public class SaveStaffServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String staffName = request.getParameter("staffName");
            String staffPhone = request.getParameter("staffPhone");
            String staffEmail = request.getParameter("staffEmail");
            String role = request.getParameter("role");

            // Generate a random password for the new staff member
            String generatedPassword = generateRandomPassword(8);

            // Create and populate Staff object
            Staff e = new Staff();
            e.setStaffName(staffName);
            e.setStaffPhone(staffPhone);
            e.setStaffEmail(staffEmail);
            e.setStaffPassword(generatedPassword); // Set the generated password
            e.setRole(role);

            System.out.println("Attempting to save record..."); // Debugging statement

            int status = staffDAO.save(e);
            if (status > 0) {
                // Send an email with the generated password
                if (sendEmail(staffEmail, staffName, generatedPassword, role)) {
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Record saved successfully! An email with login credentials has been sent.');");
                    out.println("window.location.href = 'staff_register.jsp';");
                    out.println("</script>");
                } else {
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Record saved, but email sending failed.');");
                    out.println("window.location.href = 'staff_register.jsp';");
                    out.println("</script>");
                }
            } else {
                out.println("Sorry! unable to save record");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            out.println("Exception occurred: " + ex.getMessage());
        } finally {
            out.close();
        }
    }

    private String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    private boolean sendEmail(String to, String staffName, String generatedPassword, String role) {
        final String from = "nadiyatulh18@gmail.com"; // Sender email
        final String password = "rgku epjt eafj wcij"; // App password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(from, password);
            }
        });
        
        session.setDebug(true); //to see detailed email logs

        try {
            
            System.out.println("Preparing to send email to: " + to); // ✅ Debug print
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Welcome to the Company!");
            message.setText("Dear " + staffName + ",\n\n"
                    + "Congratulations! You have been successfully registered as "+role+" in Coin Laundry.\n"
                    + "Here are your login credentials:\n"
                    + "Email: " + to + "\n"
                    + "Password: " + generatedPassword + "\n\n"
                    + "Please log in and change your password upon first login.\n\n"
                    + "Best regards,\n"
                    + "Management Team");

            Transport.send(message);
            System.out.println("Email sent successfully.");
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles saving staff and sending login credentials.";
    }
}
