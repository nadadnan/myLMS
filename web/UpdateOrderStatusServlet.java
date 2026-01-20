/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.DAO.ordersDAO;
import com.DAO.staffDAO;
import com.Model.Staff;
import com.Util.EmailUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UpdateOrderStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Staff> staffList = staffDAO.getAllUsers();
        request.setAttribute("staffList", staffList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("view_orders.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderID = Integer.parseInt(request.getParameter("orderID"));
            String newStatus = request.getParameter("orderStatus");

            ordersDAO orderDAO = new ordersDAO();
            boolean success = orderDAO.updateOrderStatus(orderID, newStatus);

            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("staffName") == null || session.getAttribute("role") == null) {
                response.sendRedirect("staff_login.jsp");
                return;
            }

            if (success) {
                // Get customer's email
                String customerEmail = orderDAO.getCustomerEmailByOrderID(orderID);

                if (customerEmail != null && !customerEmail.isEmpty()) {
                    String subject = "Your Laundry Order #" + orderID + " Status Updated";
                    String messageText = "Dear Customer,\n\nYour laundry order status has been updated to: " + newStatus +
                            ".\n\nThank you for using our service.\n\n- Laundry Management System";

                    // Send email
                    EmailUtil.sendEmail(customerEmail, subject, messageText);
                }
            }

            String role = (String) session.getAttribute("role");
            System.out.println("Logged in role: " + role);

            if (role != null) {
                role = role.trim();

                if ("Delivery Personnel".equalsIgnoreCase(role)) {
                    response.sendRedirect("view_jobs.jsp?success=1");
                } else if ("Operational Staff".equalsIgnoreCase(role)) {
                    response.sendRedirect("view_orders_ops.jsp?success=1");
                } else if ("Manager".equalsIgnoreCase(role)) {
                    response.sendRedirect("view_orders.jsp?success=1");
                } else {
                    response.sendRedirect("dashboard.jsp?success=1");  // fallback
                }
            }
        } catch (ClassNotFoundException | NumberFormatException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles updating order status for different staff roles.";
    }
}
