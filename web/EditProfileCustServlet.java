/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
@WebServlet("/EditProfileCustServlet")
public class EditProfileCustServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String custEmail = (String) session.getAttribute("custEmail");

        if (custEmail == null) {
            response.sendRedirect("cust_login.jsp");
            return;
        }

        String custName = request.getParameter("custName");
        String custPhone = request.getParameter("custPhone");
        String custPassword = request.getParameter("custPassword");
        String custAddress = request.getParameter("custAddress");

        StringBuilder sqlBuilder = new StringBuilder("UPDATE customer SET ");
        List<Object> parameters = new ArrayList<>();

        if (custName != null && !custName.trim().isEmpty()) {
            sqlBuilder.append("custName = ?, ");
            parameters.add(custName.trim());
        }
        if (custPhone != null && !custPhone.trim().isEmpty()) {
            sqlBuilder.append("custPhone = ?, ");
            parameters.add(custPhone.trim());
        }
        if (custPassword != null && !custPassword.trim().isEmpty()) {
            sqlBuilder.append("custPassword = ?, ");
            parameters.add(custPassword.trim());
        }
        if (custAddress != null && !custAddress.trim().isEmpty()) {
            sqlBuilder.append("custAddress = ?, ");
            parameters.add(custAddress.trim());
        }

        // No fields to update
        if (parameters.isEmpty()) {
            response.sendRedirect("profile.jsp");
            return;
        }

        // Remove the last comma and add WHERE clause
        sqlBuilder.setLength(sqlBuilder.length() - 2);
        sqlBuilder.append(" WHERE custEmail = ?");
        parameters.add(custEmail);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            stmt.executeUpdate();

            // Update session attributes
            if (custName != null && !custName.trim().isEmpty()) session.setAttribute("custName", custName);
            if (custPhone != null && !custPhone.trim().isEmpty()) session.setAttribute("custPhone", custPhone);
            if (custPassword != null && !custPassword.trim().isEmpty()) session.setAttribute("custPassword", custPassword);
            if (custAddress != null && !custAddress.trim().isEmpty()) session.setAttribute("custAddress", custAddress);

            response.sendRedirect("profile.jsp");

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
