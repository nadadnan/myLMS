/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.DAO.DBUtil;
import com.DAO.ordersDAO;
import com.DAO.staffDAO;
import com.Model.Orders;
import com.Model.Staff;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author M S I
 */
public class AssignStaffServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        // Get the list of staff
        List<Staff> staffList = staffDAO.getAllUsers();

        // Get all orders
        ordersDAO orderDAO = new ordersDAO();
        List<Orders> allOrders = orderDAO.getAllOrders();

        // Set attributes
        request.setAttribute("staffList", staffList);
        request.setAttribute("allOrders", allOrders);

        // Forward
        RequestDispatcher dispatcher = request.getRequestDispatcher("view_orders.jsp");
        dispatcher.forward(request, response);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
        throw new ServletException(e); // Optionally re-throw as ServletException
    }
}

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Parse the orderID and staffID from request
            int orderID = Integer.parseInt(request.getParameter("orderID"));
            int staffID = Integer.parseInt(request.getParameter("staffID"));
            
            // Create an instance of ordersDAO
            ordersDAO orderDAO = new ordersDAO();
            
            // Attempt to assign the staff to the order
            boolean success = orderDAO.assignStaffToOrder(orderID, staffID);
            
            if (success) {
                // If assignment is successful, set a success message
                request.setAttribute("message", "Staff assigned successfully!");
            } else {
                // If assignment fails, set an error message
                request.setAttribute("error", "Failed to assign staff. Try again.");
            }
            
            // Forward to the orders page (You can use request dispatcher for better handling of messages)
            request.getRequestDispatcher("view_orders.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            // Handle number format exception for invalid input
            e.printStackTrace();
            request.setAttribute("error", "Invalid input format! Please provide valid Order and Staff IDs.");
            request.getRequestDispatcher("manageOrders.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle any other exceptions
            e.printStackTrace();
            request.setAttribute("error", "An unexpected error occurred. Please try again.");
            request.getRequestDispatcher("manageOrders.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Assigns staff to an order.";
    }// </editor-fold>
}
