/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DAO;

import com.Model.laundryPackage;
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
public class laundryPackageDAO {

    public static int save(laundryPackage e) throws ClassNotFoundException{
        int status = 0;
        try (Connection con = DBUtil.getConnection()) {
            if (con != null) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO laundryPackage(packageName, packageDesc, packagePrice, packageImage) VALUES (?, ?, ?, ?)");
                ps.setString(1, e.getPackageName());
                ps.setString(2, e.getPackageDesc());
                ps.setDouble(3, e.getPackagePrice());
                ps.setString(4, e.getPackageImage());

                status = ps.executeUpdate();
                ps.close();
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error saving product: " + ex.getMessage());
        }
        return status;
    }

    public static int update(laundryPackage pack) throws ClassNotFoundException{
        int status = 0;
        try (Connection con = DBUtil.getConnection()) {
            if (con != null) {
                String query = "UPDATE laundryPackage SET packageName=?, packageDesc=?, packagePrice=?, packageImage=? WHERE packageID=?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, pack.getPackageName());
                ps.setString(2, pack.getPackageDesc());
                ps.setDouble(3, pack.getPackagePrice());
                ps.setString(4, pack.getPackageImage());
                ps.setString(5, pack.getPackageID());

                // Log the prepared statement for debugging
                System.out.println("Executing Update: " + ps);

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

    public static int delete(String packageID) throws ClassNotFoundException{
        int status = 0;
        try (Connection con = DBUtil.getConnection()) {
            if (con != null) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM laundryPackage WHERE packageID = ?");
                ps.setString(1, packageID);
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

    public static laundryPackage getPackageById(String packageID) throws ClassNotFoundException {
        laundryPackage e = new laundryPackage();
        System.out.println("Product id i ngetMenuById :" + packageID);
        try (Connection con = DBUtil.getConnection()) {
            if (con != null) {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM laundryPackage WHERE packageID=?");
                ps.setString(1, packageID);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    e.setPackageID(rs.getString("packageID"));
                    e.setPackageName(rs.getString("packageName"));
                    e.setPackageDesc(rs.getString("packageDesc"));
                    e.setPackagePrice(rs.getDouble("packagePrice"));
                    e.setPackageImage(rs.getString("packageImage"));
                }
                rs.close();
                ps.close();
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return e;
    }

    public static List<laundryPackage> getAllLaundryPackage() throws ClassNotFoundException{
        List<laundryPackage> laundryPackages = new ArrayList<>();
        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("select * from laundryPackage");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                laundryPackage pkg = new laundryPackage();
                pkg.setPackageID(rs.getString("packageID"));
                pkg.setPackageName(rs.getString("packageName"));
                pkg.setPackageDesc(rs.getString("packageDesc"));
                pkg.setPackagePrice(rs.getDouble("packagePrice"));
                pkg.setPackageImage(rs.getString("packageImage"));
                laundryPackages.add(pkg);
            }

            System.out.println("Number of packages retrieved: " + laundryPackages.size()); // Debugging line

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return laundryPackages;
    }

}
