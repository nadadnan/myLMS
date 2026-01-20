/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.DAO.customerDAO;
import com.Model.Customer;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SaveCustServlet")
public class SaveCustServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        List<String> validPostalCodes = customerDAO.getValidPostalCodes(); // Fetch from DB
        request.getSession().setAttribute("validPostalCodes", validPostalCodes);

        try {
            // Retrieve form data
            String custName = request.getParameter("custName");
            String custPhone = request.getParameter("custPhone");
            String custEmail = request.getParameter("custEmail");
            String custPassword = request.getParameter("custPassword");
            String custAddress = request.getParameter("custAddress");
            String postalCode = request.getParameter("postalCode").trim();
            
            //String hashedPassword = customerDAO.hashPassword(plainPassword);

            // Validate postal code
            boolean isPostalCodeValid = customerDAO.validatePostalCode(postalCode, "registration");
            if (!isPostalCodeValid) {
                // Invalid postal code
                request.setAttribute("status", "invalidPostalCode");
                request.setAttribute("message", "Sorry, we do not currently serve your area.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("cust_register.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Set Customer details
            Customer customer = new Customer();
            customer.setCustName(custName);
            customer.setCustPhone(custPhone);
            customer.setCustEmail(custEmail);
            customer.setCustPassword(custPassword);
            customer.setCustAddress(custAddress);
            customer.setPostalCode(postalCode);

            // Save Customer to DB
            int status = customerDAO.save(customer);

            // Redirect or display message based on status
            if (status > 0) {
                // Successful registration
                request.setAttribute("status", "success");
                RequestDispatcher dispatcher = request.getRequestDispatcher("cust_login.jsp");
                dispatcher.forward(request, response);
            } else {
                // Registration failed
                request.setAttribute("status", "failed");
                //response.getWriter().println("<h3>Registration Failed. Please try again!</h3>");
                RequestDispatcher dispatcher = request.getRequestDispatcher("cust_register.jsp");
                dispatcher.forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Error: " + e.getMessage());
            request.getRequestDispatcher("cust_register.jsp").forward(request, response);
        }
    }
}
