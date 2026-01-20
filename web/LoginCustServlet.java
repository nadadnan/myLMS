/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.DAO.DBUtil;
/**
 *
 * Author: NUR NADIYATUL HUSNA BINTI ADNAN (S65470)
 */
@WebServlet(name = "LoginCustServlet", urlPatterns = {"/LoginCustServlet"})
public class LoginCustServlet extends HttpServlet {

    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String custEmail = request.getParameter("custEmail");
        String custPassword = request.getParameter("custPassword");

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // ✅ Use your DBUtil
            conn = DBUtil.getConnection();

            String query = "SELECT * FROM customer WHERE custEmail = ? AND custPassword = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, custEmail);
            pst.setString(2, custPassword);
            rs = pst.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("custID", rs.getString("custID"));
                session.setAttribute("custName", rs.getString("custName"));
                session.setAttribute("custEmail", rs.getString("custEmail"));
                session.setAttribute("custPassword", rs.getString("custPassword"));
                session.setAttribute("custPhone", rs.getString("custPhone"));
                session.setAttribute("custAddress", rs.getString("custAddress"));

                request.getRequestDispatcher("cust_dashboard1.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Invalid email or password.");
                request.getRequestDispatcher("cust_login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { }
            try { if (pst != null) pst.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
    }

        
    }



