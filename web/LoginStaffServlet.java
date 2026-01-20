/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import java.io.IOException;
import java.sql.Connection;
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
 * @author M S I
 */
@WebServlet(name = "LoginStaffServlet", urlPatterns = {"/LoginStaffServlet"})
public class LoginStaffServlet extends HttpServlet {
private String mapRole(String inputRole) {
    switch (inputRole) {
        case "manager":
            return "Manager";
        case "staff":
            return "Operational Staff";
        case "delPerson":
            return "Delivery Personnel";
        default:
            return "";
    }
}

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String staffEmail = request.getParameter("staffEmail");
        String staffPassword = request.getParameter("staffPassword");
        String inputRole = request.getParameter("role");

        String mappedRole = mapRole(inputRole);

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // ✅ Use your DBUtil class for DB connection
            conn = DBUtil.getConnection();

            String sql = "SELECT * FROM staff WHERE staffEmail = ? AND staffPassword = ? AND role = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, staffEmail);
            stmt.setString(2, staffPassword);
            stmt.setString(3, mappedRole);

            rs = stmt.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("staffID", rs.getInt("staffID"));
                session.setAttribute("staffName", rs.getString("staffName"));
                session.setAttribute("staffEmail", rs.getString("staffEmail"));
                session.setAttribute("staffPassword", rs.getString("staffPassword"));
                session.setAttribute("staffPhone", rs.getString("staffPhone"));
                session.setAttribute("role", rs.getString("role"));
                session.setMaxInactiveInterval(30 * 60); // 30 minutes

                // Redirect based on role
                String role = rs.getString("role");
                switch (role) {
                    case "Manager":
                        response.sendRedirect("NewOrderCountServlet");
                        break;
                    case "Operational Staff":
                        response.sendRedirect("staff_dashboard.jsp");
                        break;
                    case "Delivery Personnel":
                        response.sendRedirect("delPerson_dashboard.jsp");
                        break;
                    default:
                        response.sendRedirect("staff_login.jsp?error=invalidRole");
                        break;
                }
            } else {
                // Invalid login
                response.sendRedirect("staff_login.jsp?error=invalid");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("staff_login.jsp?error=exception");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet for staff login with role-based redirection";
    }
}