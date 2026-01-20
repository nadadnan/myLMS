/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.DAO.laundryPackageDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author M S I
 */
@WebServlet("/DeletePackageServlet")
public class DeletePackageServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String packageID = request.getParameter("packageID");

        // Debugging statement
        System.out.println("Received packageID to delete: " + packageID);

        if (packageID == null || packageID.isEmpty()) {
            response.getWriter().println("<p>Error: Package ID is missing</p>");
            return;
        }

        try {
            int status = laundryPackageDAO.delete(packageID);

            // Debugging statement
            System.out.println("Delete status: " + status);

            if (status > 0) {
                String messageScript = "<script>"
                        + "alert('Package deleted successfully');"
                        + "window.location.href = 'managePackage.jsp';"
                        + "</script>";
                response.getWriter().println(messageScript);
            } else {
                response.getWriter().println("<p>Error: Unable to delete package</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error: " + e.getMessage() + "</p>");
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
        return "Handles deletion of laundry packages.";
    }
}
