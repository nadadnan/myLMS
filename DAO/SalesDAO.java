/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DAO;

import com.DAO.DBUtil;
import com.Model.PackagePopularity;
import java.sql.*;
import java.util.*;

public class SalesDAO {
    
    public Map<String, Double> getMonthlySales() {
    Map<String, Double> salesData = new LinkedHashMap<>();
    
    try {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT DATE_FORMAT(pickupDate, '%Y-%m') AS month, SUM(totalPrice) AS total " +
                     "FROM orders GROUP BY month ORDER BY month";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String month = rs.getString("month");
            double total = rs.getDouble("total");
            salesData.put(month, total);
        }

        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    return salesData;
}
    
    public static List<PackagePopularity> getPackagePopularityReport() {
    List<PackagePopularity> report = new ArrayList<>();

    try {
        Connection con = DBUtil.getConnection();
        String sql = "SELECT lp.packageName, COUNT(oi.packageID) AS timesOrdered, " +
                     "SUM(oi.quantity * lp.packagePrice) AS revenueGenerated " +
                     "FROM order_items oi " +
                     "JOIN laundrypackage lp ON oi.packageID = lp.packageID " +
                     "GROUP BY lp.packageName " +
                     "ORDER BY revenueGenerated DESC";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println("Found package: " + rs.getString("packageName")); // Debug line
            
            PackagePopularity pp = new PackagePopularity();
            pp.setPackageName(rs.getString("packageName"));
            pp.setTimesOrdered(rs.getInt("timesOrdered"));
            pp.setRevenueGenerated(rs.getDouble("revenueGenerated"));
            report.add(pp);
        }

        con.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    return report;
}

}
