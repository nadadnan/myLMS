package com.WEB;

import com.DAO.DBUtil;
import com.DAO.customerDAO;
import com.DAO.ordersDAO;
import com.Model.Cart;
import com.Model.CartItem;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import javax.servlet.http.HttpSession;

public class SaveOrderServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    
    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
        HttpSession session = request.getSession();
        String custID = (String) session.getAttribute("custID");
        Cart cart = (Cart) session.getAttribute("cart");

        String pickupDate = request.getParameter("pickupDate");
        String pickupTime = request.getParameter("pickupTime");
        double totalPrice = Double.parseDouble(request.getParameter("totalPrice"));

        if (custID == null || cart == null || cart.isEmpty()) {
            response.sendRedirect("cart.jsp?error=SessionOrCartEmpty");
            return;
        }

    //String pickupDate = request.getParameter("pickupDate");
    //String pickupTime = request.getParameter("pickupTime");
    String totalPriceStr = request.getParameter("totalPrice");
    String[] selectedPackages = request.getParameterValues("packageID");
    String[] quantities = request.getParameterValues("quantity"); // Assuming you send this in form
    String[] prices = request.getParameterValues("pricePerUnit"); // Assuming you send this too

    //double totalPrice = 0;
    if (totalPriceStr != null && !totalPriceStr.trim().isEmpty()) {
        try {
            totalPrice = Double.parseDouble(totalPriceStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid total price. Please enter a valid number.");
            return;
        }
    } else {
        response.getWriter().write("Total price is required.");
        return;
    }

    //String custID = (String) request.getSession().getAttribute("custID");

    try {
        Connection conn = DBUtil.getConnection();
        String query = "INSERT INTO orders (custID, pickupDate, pickupTime, totalPrice) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, custID);
        stmt.setString(2, pickupDate);
        stmt.setString(3, pickupTime);
        stmt.setDouble(4, totalPrice);

        int rowsAffected = stmt.executeUpdate();

        if (rowsAffected > 0) {
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            int orderID = -1;

            if (generatedKeys.next()) {
                orderID = generatedKeys.getInt(1);
            }

            // Insert into order_items
            String insertItemsQuery = "INSERT INTO order_items (orderID, packageID, quantity, pricePerUnit) VALUES (?, ?, ?, ?)";
            PreparedStatement itemStmt = conn.prepareStatement(insertItemsQuery);

            for (CartItem item : cart.getItems().values()) {
                itemStmt.setInt(1, orderID);
                itemStmt.setString(2, item.getLaundryPackage().getPackageID());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getLaundryPackage().getPackagePrice());
                itemStmt.addBatch();
}

                itemStmt.executeBatch(); // Execute batch insert
                itemStmt.close();
            

            // Retrieve latest order details
            String queryOrder = "SELECT pickupDate, pickupTime FROM orders WHERE custID = ? ORDER BY orderID DESC LIMIT 1";
            PreparedStatement orderStmt = conn.prepareStatement(queryOrder);
            orderStmt.setString(1, custID);
            ResultSet orderRs = orderStmt.executeQuery();

            if (orderRs.next()) {
                String confirmedPickupDate = orderRs.getString("pickupDate");
                String confirmedPickupTime = orderRs.getString("pickupTime");
                String custAddress = getCustomerAddress(custID);

                request.setAttribute("custAddress", custAddress);
                request.setAttribute("pickupDate", confirmedPickupDate);
                request.setAttribute("pickupTime", confirmedPickupTime);

                request.getRequestDispatcher("ordersConfirmation.jsp").forward(request, response);
            } else {
                response.getWriter().write("Order details not found.");
            }

            orderStmt.close();
            stmt.close();
            conn.close();
        } else {
            response.getWriter().write("Error saving order. Please try again.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        response.getWriter().write("Database error: " + e.getMessage());
    }
}


    private String getCustomerAddress(String custID) {
        String custAddress = null;
        try {
            Connection conn = DBUtil.getConnection();
            String query = "SELECT custAddress FROM customer WHERE custID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, custID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                custAddress = rs.getString("custAddress");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return custAddress;
    }

    @Override
    public String getServletInfo() {
        return "Save Order Servlet";
    }
}
 