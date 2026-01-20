/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.DAO.ordersDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CancelOrderServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String orderIDStr = request.getParameter("orderID");
        System.out.println("Received orderID string: " + orderIDStr);

        if (orderIDStr == null || orderIDStr.trim().isEmpty() || orderIDStr.equalsIgnoreCase("null")) {
            response.getWriter().println("<script>alert('Error: Order ID is missing or invalid.'); window.location.href = 'cust_dashboard1.jsp';</script>");
            return;
        }

        try {
            // Extract only digits from the order ID string
            String numericPart = orderIDStr.replaceAll("\\D+", "");

            if (numericPart.isEmpty()) {
                throw new NumberFormatException("No numeric part found in orderID");
            }

            int orderID = Integer.parseInt(numericPart);
            System.out.println("Parsed orderID (int): " + orderID);

            int status = ordersDAO.delete(orderID);
            System.out.println("Delete status: " + status);

            if (status > 0) {
                response.getWriter().println("<script>alert('Order cancelled successfully.'); window.location.href = 'cust_dashboard1.jsp';</script>");
            } else {
                response.getWriter().println("<script>alert('Unable to cancel order. It may not exist or has already been processed.'); window.location.href = 'cust_dashboard1.jsp';</script>");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.getWriter().println("<script>alert('Error: Invalid order ID format.'); window.location.href = 'cust_dashboard1.jsp';</script>");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<script>alert('An unexpected error occurred: " + e.getMessage() + "'); window.location.href = 'cust_dashboard1.jsp';</script>");
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
        return "Handles cancellation of customer orders.";
    }
}


    /*
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the session and ensure the user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("custID") == null) {
            response.sendRedirect("cust_login.jsp");
            return;
        }

        String custID = (String) session.getAttribute("custID");
        String orderIDStr = request.getParameter("orderID");

        // Validate that orderID was provided
        if (orderIDStr == null || orderIDStr.trim().isEmpty()) {
            response.sendRedirect("orderHistory.jsp?error=MissingOrderID");
            return;
        }

        try {
            int orderID = Integer.parseInt(orderIDStr); // Safe parse after null/empty check

            // Optional: remove cart from session if relevant
            session.removeAttribute("cart");

            // Attempt to delete the order
            boolean deleted = removeOrderFromDatabase(custID, orderID);

            if (deleted) {
                response.sendRedirect("orderHistory.jsp?success=OrderCancelled");
            } else {
                response.sendRedirect("orderHistory.jsp?error=OrderNotFound");
            }

        } catch (NumberFormatException e) {
            // If parsing orderID failed
            e.printStackTrace();
            response.sendRedirect("orderHistory.jsp?error=InvalidOrderID");
        } catch (Exception e) {
            // Handle database or other exceptions
            e.printStackTrace();
            response.sendRedirect("orderHistory.jsp?error=ServerError");
        }
    }

 
    private boolean removeOrderFromDatabase(String custID, int orderID) throws Exception {
        boolean success = false;

        try (Connection conn = DBUtil.getConnection()) {
            if (conn != null) {
                String deleteOrderQuery = "DELETE FROM orders WHERE orderID = ? AND custID = ?";
                PreparedStatement ps = conn.prepareStatement(deleteOrderQuery);
                ps.setInt(1, orderID);
                ps.setString(2, custID);

                int rowsAffected = ps.executeUpdate();
                success = (rowsAffected > 0);

                ps.close();
            } else {
                System.out.println("Database connection failed.");
            }
        }

        return success;
    }

    @Override
    public String getServletInfo() {
        return "Servlet to handle customer order cancellation.";
    }
    */

