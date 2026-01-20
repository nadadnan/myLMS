/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.DAO.customerDAO;
import com.Model.CoverageArea;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditCoverageAreaServlet2 extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            // Retrieve form parameters
            String sid = request.getParameter("Id"); // 'Id' matches the parameter in the JSP
            int id = Integer.parseInt(sid); // Parse to integer
            String postal_code = request.getParameter("postal_code");
            String area_name = request.getParameter("area_name");

            // Update the coverage area in the database
            CoverageArea area = new CoverageArea();
            area.setId(id); // Set ID for the existing record
            area.setPostal_code(postal_code); // Update postal code
            area.setArea_name(area_name); // Update area name

            int status = customerDAO.update(area); // DAO method to update the record
            if (status > 0) {
                request.setAttribute("message", "Record updated successfully!");
                request.getRequestDispatcher("coverageArea.jsp").forward(request, response);
            } else {
                request.setAttribute("message", "Sorry! Unable to update record.");
                request.getRequestDispatcher("coverageArea.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("message", "Error: " + e.getMessage());
            request.getRequestDispatcher("coverageArea.jsp").include(request, response);
        } finally {
            out.close();
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
        return "Servlet to update coverage area details";
    }
}
