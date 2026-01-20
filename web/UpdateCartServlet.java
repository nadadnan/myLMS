/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.DAO.ordersDAO;
import com.Model.Cart;
import com.Model.CartItem;
import com.Model.laundryPackage;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;


/**
 *
 * @author M S I
 */
public class UpdateCartServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("application/json"); // Set response type
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession();

    // Retrieve or create the cart
    Cart cart = (Cart) session.getAttribute("cart");
    if (cart == null) {
        cart = new Cart();
        session.setAttribute("cart", cart);
    }

    // Process the quantity update
    for (String param : request.getParameterMap().keySet()) {
        if (param.startsWith("quantity_")) {
            String packageID = param.substring("quantity_".length());
            String quantityStr = request.getParameter(param);

            try {
                int newQuantity = Integer.parseInt(quantityStr);
                CartItem item = cart.getItems().get(packageID); // Get item by packageID
                if (item != null) {
                    item.setQuantity(newQuantity); // Update quantity
                }
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Log invalid input
            }
        }
    }

    // Prepare the response data
    double totalPrice = cart.getTotalPrice();
    double deliveryFee = 10; // Fixed delivery fee
    double grandTotal = totalPrice + deliveryFee;

    Map<String, Double> responseMap = new HashMap<>();
    responseMap.put("totalPrice", totalPrice);
    responseMap.put("grandTotal", grandTotal);

    // Send JSON response
    out.write(new Gson().toJson(responseMap));
    out.flush();
}

    @Override
    public String getServletInfo() {
        return "UpdateCartServlet to update cart items and calculate total price.";
    }
}