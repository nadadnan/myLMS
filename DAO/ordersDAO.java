/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DAO;

import com.DAO.DBUtil;
import com.Model.Orders;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ordersDAO {

    // Save a new order into the database
    public int saveOrder(Orders order) {
        int orderID = 0;
        try (Connection con = DBUtil.getConnection()) {
            if (con != null) {
                String query = "INSERT INTO orders (custID, pickupDate, pickupTime, totalPrice) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, order.getCustID());
                stmt.setDate(2, java.sql.Date.valueOf(order.getPickupDate())); // Convert LocalDate to SQL Date
                stmt.setTime(3, java.sql.Time.valueOf(order.getPickupTime())); // Convert LocalTime to SQL Time
                stmt.setDouble(4, order.getTotalPrice());
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    orderID = rs.getInt(1); // Get the generated order ID
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderID;
    }

    // Save a single order detail item
    public boolean saveOrderDetail(int orderID, String packageID, int quantity) {
        boolean isSaved = false;
        try (Connection con = DBUtil.getConnection()) {
            if (con != null) {
                String query = "INSERT INTO order_detail (orderID, packageID, quantity) VALUES (?, ?, ?)";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, orderID);
                stmt.setString(2, packageID);
                stmt.setInt(3, quantity);

                int rowsAffected = stmt.executeUpdate();
                isSaved = rowsAffected > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSaved;
    }
    
    public static int delete(int orderID) throws ClassNotFoundException {
    int status = 0;

    try (Connection con = DBUtil.getConnection()) {
        if (con != null) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM orders WHERE orderID = ?");
            ps.setInt(1, orderID);
            status = ps.executeUpdate();
            ps.close();
        } else {
            System.out.println("Connection to database failed.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return status;
}
    
    // Get orders by customer ID and join with order_items to get item details
    public static List<Orders> getOrdersByCustomerId(String cust_id) {
        List<Orders> orders = new ArrayList<>();
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(
                "SELECT o.orderID, o.custID, o.pickupDate, o.pickupTime, o.totalPrice, oi.quantity, oi.totalItemPrice "
                + "FROM orders o "
                + "JOIN order_items oi ON o.orderID = oi.orderID "
                + "WHERE o.custID = ?")) {

            ps.setString(1, cust_id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Orders order = new Orders();
                    order.setOrderID(rs.getInt("orderID"));
                    order.setCustID(rs.getString("custID"));
                    order.setPickupDate(rs.getDate("pickupDate").toLocalDate());
                    order.setPickupTime(rs.getTime("pickupTime").toLocalTime());
                    order.setTotalPrice(rs.getDouble("totalPrice"));
                    // Instead of quantity from orders, get it from order_items
                    order.setQuantity(rs.getInt("quantity"));
                    orders.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Update the total price of an existing order
    public static boolean updateOrderTotalPrice(int orderID, double newTotalPrice) {
        boolean isUpdated = false;
        try (Connection con = DBUtil.getConnection()) {
            if (con != null) {
                String query = "UPDATE orders SET totalPrice = ? WHERE orderID = ?";
                try (PreparedStatement ps = con.prepareStatement(query)) {
                    ps.setDouble(1, newTotalPrice);
                    ps.setInt(2, orderID);

                    int rowsAffected = ps.executeUpdate();
                    isUpdated = rowsAffected > 0;
                    System.out.println("Order total price updated. Rows affected: " + rowsAffected);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    // Update an existing order
    public static boolean updateOrder(Orders order) {
        boolean isUpdated = false;
        try (Connection con = DBUtil.getConnection()) {
            String query = "UPDATE orders SET pickupDate = ?, pickupTime = ?, totalPrice = ? WHERE orderID = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setDate(1, Date.valueOf(order.getPickupDate()));
                ps.setTime(2, Time.valueOf(order.getPickupTime()));
                ps.setDouble(3, order.getTotalPrice());
                ps.setInt(4, order.getOrderID());

                int rowsAffected = ps.executeUpdate();
                isUpdated = rowsAffected > 0;
                System.out.println("Order updated. Rows affected: " + rowsAffected);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    // Method to fetch order history for a specific customer
    public static List<Orders> getOrderHistoryByCustomer(String custID) throws ClassNotFoundException {
        List<Orders> orderHistory = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE custID = ? ORDER BY pickupDate DESC, pickupTime DESC";  // Adjust based on your table structure

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, custID);  // Set the customer ID in the query

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Retrieve each order details
                    int orderID = rs.getInt("orderID");
                    LocalDate pickupDate = rs.getDate("pickupDate").toLocalDate();  // Convert SQL date to LocalDate
                    LocalTime pickupTime = rs.getTime("pickupTime").toLocalTime();  // Convert SQL time to LocalTime
                    double totalPrice = rs.getDouble("totalPrice");
                    int quantity = rs.getInt("quantity");

                    // Create Orders object and populate it with data
                    Orders order = new Orders();
                    order.setOrderID(orderID);
                    order.setCustID(custID);
                    order.setPickupDate(pickupDate);
                    order.setPickupTime(pickupTime);
                    order.setTotalPrice(totalPrice);
                    order.setQuantity(quantity);

                    orderHistory.add(order);  // Add the order to the list
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderHistory;  // Return the list of orders
    }

    public static boolean updateOrderPayment(int orderID, int paymentID) throws ClassNotFoundException{
        boolean isUpdated = false;
        try (Connection con = DBUtil.getConnection()) {
            String query = "UPDATE orders SET paymentID = ? WHERE orderID = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, paymentID);
                ps.setInt(2, orderID);

                int rowsAffected = ps.executeUpdate();
                isUpdated = rowsAffected > 0;
                System.out.println("Payment updated for Order ID: " + orderID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    //count orders in
    public int getNewOrdersCount() throws ClassNotFoundException{
        int count = 0;
        try {
            Connection conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM orders WHERE orderStatus = 'Pending'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // Fetch pending orders
    public List<Orders> getPendingOrders() throws ClassNotFoundException {
        List<Orders> pendingOrders = new ArrayList<>();
        //String query = "SELECT * FROM orders WHERE orderStatus = 'Pending'";
        String query = "SELECT o.orderID, o.custID, c.custAddress, o.pickupDate, o.pickupTime, o.totalPrice, o.orderStatus "
                + "FROM orders o "
                + "JOIN customer c ON o.custID = c.custID "
                + "WHERE o.orderStatus = 'Pending'";

        try (Connection con = DBUtil.getConnection(); PreparedStatement pstmt = con.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Orders order = new Orders();
                order.setOrderID(rs.getInt("orderID"));
                order.setCustID(rs.getString("custID"));
                order.setCustAddress(rs.getString("custAddress"));
                order.setPickupDate(rs.getDate("pickupDate").toLocalDate()); // Ensure correct conversion
                order.setPickupTime(rs.getTime("pickupTime").toLocalTime()); // Convert SQL Time to LocalTime
                order.setTotalPrice(rs.getDouble("totalPrice"));
                order.setOrderStatus(rs.getString("orderStatus"));

                pendingOrders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingOrders;
    }

    public boolean updateOrderStatus(int orderID, String newStatus) throws ClassNotFoundException {
        String query = "UPDATE orders SET orderStatus = ? WHERE orderID = ?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderID);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0; // Returns true if update is successful

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean assignStaffToOrder(int orderID, int staffID) throws ClassNotFoundException {
        String query = "UPDATE orders SET assignedStaffID = ?, orderStatus = 'Assigned' WHERE orderID = ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, staffID);
            pst.setInt(2, orderID);

            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Orders> getPendingOrdersWithStaff() throws ClassNotFoundException {
        List<Orders> pendingOrders = new ArrayList<>();
        String query = "SELECT o.orderID, o.custID, c.custAddress, o.pickupDate, o.pickupTime, o.totalPrice, o.orderStatus, o.staffID "
                + "FROM orders o "
                + "JOIN customer c ON o.custID = c.custID "
                + "WHERE o.orderStatus = 'Pending'";

        try (Connection con = DBUtil.getConnection(); PreparedStatement pstmt = con.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Orders order = new Orders();
                order.setOrderID(rs.getInt("orderID"));
                order.setCustID(rs.getString("custID"));
                order.setCustAddress(rs.getString("custAddress"));
                order.setPickupDate(rs.getDate("pickupDate").toLocalDate());
                order.setPickupTime(rs.getTime("pickupTime").toLocalTime());
                order.setTotalPrice(rs.getDouble("totalPrice"));
                order.setOrderStatus(rs.getString("orderStatus"));
                order.setStaffID(rs.getInt("staffID")); // Ensure staffID is included

                pendingOrders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingOrders;
    }

    public static List<Orders> getAllOrders() throws ClassNotFoundException{
        List<Orders> list = new ArrayList<>();
        String query = "SELECT o.*, s.staffName FROM orders o LEFT JOIN staff s ON o.assignedStaffID = s.staffID";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Orders order = new Orders();
                order.setOrderID(rs.getInt("orderID"));
                order.setCustID(rs.getString("custID"));
                order.setPickupDate(rs.getDate("pickupDate") != null ? rs.getDate("pickupDate").toLocalDate() : null);
                order.setPickupTime(rs.getTime("pickupTime") != null ? rs.getTime("pickupTime").toLocalTime() : null);
                order.setTotalPrice(rs.getDouble("totalPrice"));
                order.setOrderStatus(rs.getString("orderStatus"));
                order.setCustAddress(""); // If needed, pull from customer table
                order.setQuantity(0); // Optional: pull from order_items table if it exists
                String assignedStaffID = rs.getString("assignedStaffID");

                if (assignedStaffID != null) {
                    order.setStaffID(Integer.parseInt(assignedStaffID));
                }

                order.setAssignedStaffName(rs.getString("staffName"));
                list.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Orders> getAllAssignedOrders() throws ClassNotFoundException{
        List<Orders> allOrders = new ArrayList<>();
        String query = "SELECT o.orderID, o.custID, c.custAddress, o.pickupDate, o.pickupTime, o.totalPrice, o.orderStatus, "
                + "o.assignedStaffID, s.staffName "
                + "FROM orders o "
                + "JOIN customer c ON o.custID = c.custID "
                + "JOIN staff s ON o.assignedStaffID = s.staffID "
                + // Changed LEFT JOIN to JOIN
                "ORDER BY o.pickupDate DESC, o.pickupTime DESC";

        try (Connection con = DBUtil.getConnection(); PreparedStatement pstmt = con.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Orders order = new Orders();
                order.setOrderID(rs.getInt("orderID"));
                order.setCustID(rs.getString("custID"));
                order.setCustAddress(rs.getString("custAddress"));
                order.setPickupDate(rs.getDate("pickupDate").toLocalDate());
                order.setPickupTime(rs.getTime("pickupTime").toLocalTime());
                order.setTotalPrice(rs.getDouble("totalPrice"));
                order.setOrderStatus(rs.getString("orderStatus"));
                order.setStaffID(rs.getInt("assignedStaffID"));
                order.setAssignedStaffName(rs.getString("staffName"));

                allOrders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allOrders;
    }

    public List<Orders> getOrdersByStaffID(int staffID) throws ClassNotFoundException {
        List<Orders> ordersList = new ArrayList<>();

        String query = "SELECT o.orderID, o.custID, c.custAddress, o.pickupDate, o.pickupTime, o.totalPrice, o.orderStatus, o.assignedStaffID, s.staffName "
                + "FROM orders o "
                + "JOIN customer c ON o.custID = c.custID "
                + "JOIN staff s ON o.assignedStaffID = s.staffID "
                + "WHERE o.assignedStaffID = ?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, staffID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Orders order = new Orders();
                order.setOrderID(rs.getInt("orderID"));
                order.setCustID(rs.getString("custID"));
                order.setCustAddress(rs.getString("custAddress"));
                order.setPickupDate(rs.getDate("pickupDate").toLocalDate());
                order.setPickupTime(rs.getTime("pickupTime").toLocalTime());
                order.setTotalPrice(rs.getDouble("totalPrice"));
                order.setOrderStatus(rs.getString("orderStatus"));
                order.setStaffID(rs.getInt("assignedStaffID"));
                order.setAssignedStaffName(rs.getString("staffName"));
                ordersList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordersList;
    }

    //operational staff
    public List<Orders> getOrdersByStatus(String status) {
        List<Orders> ordersList = new ArrayList<>();
        try {
            Connection con = DBUtil.getConnection();
            String query = "SELECT * FROM orders WHERE orderStatus = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Orders order = new Orders();
                order.setOrderID(rs.getInt("orderID"));
                order.setCustID(rs.getString("custID"));
                order.setPickupDate(rs.getDate("pickupDate").toLocalDate());
                order.setPickupTime(rs.getTime("pickupTime").toLocalTime());
                order.setOrderStatus(rs.getString("orderStatus"));
                ordersList.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ordersList;
    }

    public List<Orders> getOrdersForDeliveryPerson(String deliveryPerson) throws ClassNotFoundException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Orders> orders = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM orders WHERE deliveryPerson = ? AND orderStatus = 'ready for delivery'";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, deliveryPerson);

            rs = stmt.executeQuery();
            while (rs.next()) {
                Orders order = new Orders();
                order.setOrderID(rs.getInt("orderID"));
                order.setCustID(rs.getString("custID"));
                order.setCustAddress(rs.getString("custAddress"));
                order.setPickupDate(rs.getDate("pickupDate").toLocalDate());
                order.setPickupTime(rs.getTime("pickupTime").toLocalTime());
                order.setTotalPrice(rs.getDouble("totalPrice"));
                //order.setPaymentID(rs.getInt("paymentID"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return orders;
    }

    public List<Orders> getOrdersByMultipleStatuses(String[] statuses) {
        List<Orders> ordersList = new ArrayList<>();

        if (statuses == null || statuses.length == 0) {
            return ordersList;
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM orders WHERE orderStatus IN (");

        for (int i = 0; i < statuses.length; i++) {
            queryBuilder.append("?");
            if (i < statuses.length - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(")");

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(queryBuilder.toString())) {

            for (int i = 0; i < statuses.length; i++) {
                ps.setString(i + 1, statuses[i]);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Orders order = new Orders();
                order.setOrderID(rs.getInt("orderID"));
                order.setCustID(rs.getString("custID"));
                order.setPickupDate(rs.getDate("pickupDate").toLocalDate());
                order.setPickupTime(rs.getTime("pickupTime").toLocalTime());
                order.setOrderStatus(rs.getString("orderStatus"));
                ordersList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ordersList;
    }
    
    
    public String getCustomerEmailByOrderID(int orderID) throws ClassNotFoundException {
    String email = "";
    try (Connection conn = DBUtil.getConnection()) {
        String sql = "SELECT c.custEmail FROM orders o JOIN customer c ON o.custID = c.custID WHERE o.orderID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, orderID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            email = rs.getString("custEmail");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return email;
}


}
