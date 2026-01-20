/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.DAO.staffDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author M S I
 */
public class DeleteStaffServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String sid = request.getParameter("staffID");

        // Debugging statement
        System.out.println("Received staff_id to delete: " + sid);

        if (sid == null || sid.isEmpty()) {
            response.getWriter().println("<p>Error: Staff ID is missing</p>");
            return;
        }

        try {
            int id = Integer.parseInt(sid);
            int status = staffDAO.delete(id);

            // Debugging statement
            System.out.println("Delete status: " + status);

            if (status > 0) {
                // Display popup message about deletion
                String messageScript = "<script>"
                        + "alert('Staff deleted successfully');"
                        + "window.location.href = 'staff_register.jsp';"
                        + "</script>";
                response.getWriter().println(messageScript);
            } else {
                response.getWriter().println("<p>Error: Unable to delete staff</p>");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error: Invalid Staff ID</p>");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error: " + e.getMessage() + "</p>");
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
