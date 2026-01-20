/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;

import javax.servlet.http.*;

import com.DAO.DBUtil;

public class PaymentCallbackServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("PaymentCallbackServlet POST triggered");

        String billCode = request.getParameter("billcode");
        String transactionID = request.getParameter("transaction_id");
        String orderIDParam = request.getParameter("order_id");
        String paymentStatus = request.getParameter("status");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Debug logs
        System.out.println("Received params:");
        System.out.println("billcode: " + billCode);
        System.out.println("transaction_id: " + transactionID);
        System.out.println("order_id: " + orderIDParam);
        System.out.println("status: " + paymentStatus);

        // Validate required fields
        if (billCode == null || transactionID == null || orderIDParam == null || paymentStatus == null) {
            out.println("Invalid request: missing parameters.");
            return;
        }

        try {
            int orderID = Integer.parseInt(orderIDParam.replaceAll("\\D", "")); // ORDER244 -> 244

            if ("1".equals(paymentStatus)) {
                try (Connection con = DBUtil.getConnection()) {

                    // Check if the order exists
                    String checkOrderSQL = "SELECT * FROM orders WHERE orderID = ?";
                    try (PreparedStatement checkOrder = con.prepareStatement(checkOrderSQL)) {
                        checkOrder.setInt(1, orderID);
                        try (ResultSet rs = checkOrder.executeQuery()) {
                            if (!rs.next()) {
                                out.println("Error: orderID " + orderID + " does not exist.");
                                return;
                            }
                        }
                    }

                    // Avoid duplicate payment records
                    String checkPaymentSQL = "SELECT * FROM payment WHERE transactionID = ?";
                    try (PreparedStatement checkPayment = con.prepareStatement(checkPaymentSQL)) {
                        checkPayment.setString(1, transactionID);
                        try (ResultSet rs = checkPayment.executeQuery()) {
                            if (rs.next()) {
                                out.println("ℹ Payment already recorded.");
                                return;
                            }
                        }
                    }

                    double paymentAmount = 0.0;
                    String checkBookingSQL = "SELECT totalPrice FROM orders WHERE orderID = ?";
                    try (PreparedStatement psCheck = con.prepareStatement(checkBookingSQL)) {
                        psCheck.setInt(1, orderID);
                        try (ResultSet rs = psCheck.executeQuery()) {
                            if (rs.next()) {
                                paymentAmount = rs.getDouble("totalPrice");
                            } else {
                                out.println("Error: orderID " + orderID + " does not exist.");
                                return;
                            }
                        }
                    }
                    System.out.println("DEBUG: paymentAmount = " + paymentAmount);

                    // Insert payment record              
                    String insertSQL = "INSERT INTO payment (orderID, transactionID, paymentStatus, paymentDate, paymentMethod, paymentAmount) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = con.prepareStatement(insertSQL)) {
                        ps.setInt(1, orderID);
                        ps.setString(2, transactionID);
                        ps.setString(3, "Completed");
                        ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                        ps.setString(5, "ToyyibPay");
                        ps.setDouble(6, paymentAmount);

                        int rowsInserted = ps.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Payment inserted successfully for orderID: " + orderID);
                        } else {
                            System.out.println("Failed to insert payment.");
                        }
                    }
                }

                response.sendRedirect("paymentSuccess.jsp?order_id=" + orderIDParam + "&transaction_id=" + transactionID);
            } else {
                System.out.println("Payment not completed.");
                response.sendRedirect("paymentFailed.jsp?order_id=" + orderIDParam + "&transaction_id=" + transactionID);
            }

        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Optional: Let GET also work
        doPost(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles ToyyibPay callback and stores payment in database";
    }
}
