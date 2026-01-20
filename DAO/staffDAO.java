/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DAO;

import com.DAO.DBUtil;
import com.Model.Staff;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author M S I
 */
public class staffDAO {

    public static int save(Staff e) {
        int status = 0;
        try (Connection con = DBUtil.getConnection()) {
            if (con != null) {
                String query = "INSERT INTO staff(staffName, staffPhone, staffEmail, staffPassword, role) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(query)) {
                    ps.setString(1, e.getStaffName());
                    ps.setString(2, e.getStaffPhone());
                    ps.setString(3, e.getStaffEmail());
                    ps.setString(4, e.getStaffPassword()); // Save the password set in the servlet
                    ps.setString(5, e.getRole());

                    status = ps.executeUpdate();
                    System.out.println("Staff saved with password: " + e.getStaffPassword());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error saving staff: " + ex.getMessage());
        }
        return status;
    }

    
    public static List<Staff> getAllUsers() {
        List<Staff> list = new ArrayList<Staff>();
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM staff");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Staff e = new Staff();
                e.setStaffID(rs.getInt(1)); 
                e.setStaffName(rs.getString(2));
                e.setStaffPhone(rs.getString(3));
                e.setStaffEmail(rs.getString(4));
                e.setStaffPassword(rs.getString(5));
                e.setRole(rs.getString(6));
                list.add(e);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static int delete(int staffID) {
        int status = 0;
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM staff WHERE staffID = ?");
            ps.setInt(1, staffID);
            status = ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    
    
    public static List<Staff> getStaffByRole(String role) throws ClassNotFoundException {
    List<Staff> staffList = new ArrayList<>();
    String query = "SELECT * FROM staff WHERE role = ?";

    try (Connection con = DBUtil.getConnection();
         PreparedStatement pst = con.prepareStatement(query)) {

        pst.setString(1, role);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            Staff staff = new Staff();
            staff.setStaffID(rs.getInt("staffID"));
            staff.setStaffName(rs.getString("staffName"));
            staff.setStaffEmail(rs.getString("staffEmail"));
            staff.setRole(rs.getString("role"));
            // Add other fields as needed
            staffList.add(staff);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return staffList;
}
    
    public static Staff getStaffByID(int staffID) throws ClassNotFoundException {
    String query = "SELECT * FROM staff WHERE staffID = ?";
    Staff staff = null;

    try (Connection con = DBUtil.getConnection();
         PreparedStatement pst = con.prepareStatement(query)) {

        pst.setInt(1, staffID);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            staff = new Staff();
            staff.setStaffID(rs.getInt("staffID"));
            staff.setStaffName(rs.getString("staffName"));
            staff.setStaffEmail(rs.getString("staffEmail"));
            staff.setRole(rs.getString("role"));
            // Add other fields as needed
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return staff;
}
    
    public static List<Staff> getAllStaff() throws ClassNotFoundException{
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT staffID, staffName, role FROM staff";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Staff s = new Staff();
                s.setStaffID(rs.getInt("staffID"));
                s.setStaffName(rs.getString("staffName"));
                s.setRole(rs.getString("role"));
                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
