/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DAO;

import com.DAO.DBUtil;
import com.Model.Payment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 *
 * @author M S I
 */
public class paymentDAO {
    
    // Method to save payment details
    public boolean savePayment(Payment payment) {
        boolean status = false;
        try (Connection con = DBUtil.getConnection()) {
            String query = "INSERT INTO payment (orderID, transactionID, paymentStatus, paymentDate, paymentMethod) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, payment.getOrderID());
            ps.setString(2, payment.getTransactionID());
            ps.setString(3, payment.getPaymentStatus());
            ps.setString(4, payment.getPaymentDate());
            ps.setString(5, payment.getPaymentMethod());
            
            int rowCount = ps.executeUpdate();
            if (rowCount > 0) status = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    // Method to retrieve payment details by orderID
    public Payment getPaymentByOrderID(int orderID) {
        Payment payment = null;
        try (Connection con = DBUtil.getConnection()) {
            String query = "SELECT * FROM payment WHERE orderID = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                payment = new Payment();
                payment.setPaymentID(rs.getInt("paymentID"));
                payment.setOrderID(rs.getInt("orderID"));
                payment.setTransactionID(rs.getString("transactionID"));
                payment.setPaymentStatus(rs.getString("paymentStatus"));
                payment.setPaymentDate(rs.getString("paymentDate"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payment;
    }
    
    // Method to update payment details
    public boolean updatePayment(Payment payment) {
        boolean success = false;
        try {
            Connection con = DBUtil.getConnection();
            String query = "UPDATE payment SET transactionID = ?, paymentStatus = ?, paymentDate = ? WHERE orderID = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, payment.getTransactionID());
            ps.setString(2, payment.getPaymentStatus());
            ps.setString(3, payment.getPaymentDate());
            ps.setInt(4, payment.getOrderID());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                success = true;
                System.out.println("Payment updated successfully for Order ID: " + payment.getOrderID());
            }
            
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

}
