/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.Model.Cart;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author M S I
 */
public class RemoveFromCartServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // Retrieve the packageID to be removed
        String packageID = request.getParameter("packageID");

        // Debugging statement
        System.out.println("Received packageID to remove: " + packageID);

        if (packageID == null || packageID.isEmpty()) {
            response.getWriter().println("<p>Error: Package ID is missing</p>");
            return;
        }

        try {
            // Get the session and the cart object
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");

            if (cart != null) {
                // Remove the item from the cart
                cart.removeItem(packageID);

                // Debugging statement
                System.out.println("Item removed successfully from cart");
            }

            // Redirect back to the cart page
            String messageScript = "<script>"
                    + "alert('Item removed successfully from cart');"
                    + "window.location.href = 'cart2.jsp';"
                    + "</script>";
            response.getWriter().println(messageScript);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error: " + e.getMessage() + "</p>");
        }
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
        processRequest(request, response);
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
        /*try {
            // Retrieve the packageID to remove from the cart
            String packageID = request.getParameter("packageID");

            // Validate packageID
            if (packageID == null || packageID.isEmpty()) {
                throw new IllegalArgumentException("Package ID is required to remove an item");
            }

            // Retrieve the cart from the session
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                throw new IllegalStateException("No cart found in session");
            }

            // Remove item from the cart
            cart.removeItem(packageID);

            // Save the updated cart back to the session
            session.setAttribute("cart", cart);

            // Redirect to the cart page or wherever necessary
            response.sendRedirect("cart2.jsp");

        } catch (Exception e) {
            // Handle errors and display them
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error: " + e.getMessage());
        }*/
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
