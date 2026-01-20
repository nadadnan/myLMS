/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.DAO.DBUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author M S I
 */
@WebServlet(name = "EditProfileStaffServlet", urlPatterns = {"/EditProfileStaffServlet"})
public class EditProfileStaffServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String staffEmail = (String) session.getAttribute("staffEmail");

        if (staffEmail == null) {
            response.sendRedirect("staff_login.jsp");
            return;
        }

        String staffName = request.getParameter("staffName");
        String staffPhone = request.getParameter("staffPhone");
        String staffPassword = request.getParameter("staffPassword");

        // If password is empty, fetch the existing one from the DB
        if (staffPassword == null || staffPassword.trim().isEmpty()) {
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT staffPassword FROM staff WHERE staffEmail = ?")) {
                stmt.setString(1, staffEmail);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    staffPassword = rs.getString("staffPassword"); // fallback to existing password
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp");
                return;
            }
        }

        StringBuilder sqlBuilder = new StringBuilder("UPDATE staff SET ");
        List<Object> parameters = new ArrayList<>();

        if (staffName != null && !staffName.trim().isEmpty()) {
            sqlBuilder.append("staffName = ?, ");
            parameters.add(staffName.trim());
        }
        if (staffPhone != null && !staffPhone.trim().isEmpty()) {
            sqlBuilder.append("staffPhone = ?, ");
            parameters.add(staffPhone.trim());
        }

        // Password is always present now
        sqlBuilder.append("staffPassword = ?, ");
        parameters.add(staffPassword.trim());

        // Remove last comma
        sqlBuilder.setLength(sqlBuilder.length() - 2);
        sqlBuilder.append(" WHERE staffEmail = ?");
        parameters.add(staffEmail);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            stmt.executeUpdate();

            // Always update session attributes
            if (staffName != null && !staffName.trim().isEmpty()) session.setAttribute("staffName", staffName);
            if (staffPhone != null && !staffPhone.trim().isEmpty()) session.setAttribute("staffPhone", staffPhone);
            session.setAttribute("staffPassword", staffPassword); // always set, even if old password

            response.sendRedirect("staff_profile.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
