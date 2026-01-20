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

public class EditCoverageAreaServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("Edit Coverage Area Servlet");
        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>"); 
            out.println("<title>Edit Coverage Area</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<style>");
            out.println("body { background-color: #f4f4f4; margin: 0; padding: 0; }");
            out.println(".container { max-width: 600px; margin: 50px auto; padding: 20px; background-color: #fff; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); border-radius: 8px; }");
            out.println("h1 { text-align: center; color: #333; }");
            out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            out.println("td { padding: 10px; vertical-align: top; }");
            out.println("input[type='text'], textarea { width: calc(100% - 22px); padding: 10px; margin: 5px 0; border: 1px solid #ccc; border-radius: 4px; }");
            out.println("input[type='submit'] { width: 100%; padding: 15px; background-color: #27ae60; color: #fff; border: none; border-radius: 4px; font-size: 16px; cursor: pointer; }");
            out.println("input[type='submit']:hover { background-color: #219150; }");
            out.println("label { display: block; margin-bottom: 5px; font-weight: bold; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            // Retrieve coverage area ID from request parameter
            String sid = request.getParameter("Id");
            int id = 0;
            try {
                id = Integer.parseInt(sid);
                System.out.println("Convert Success Id:" + id);
            } catch (Exception e) {
                System.out.println("Fail To convert ID");
            }

            System.out.println("Before retrieving from database");
            // Retrieve coverage area details from the database
            CoverageArea area = customerDAO.getCoverageAreaById(id);
            System.out.println("Success Retrieve from database " + area.getArea_name());

            out.println("<div class='container'>");
            out.println("<form action='EditCoverageAreaServlet2' method='post'>");
            out.println("<h1>Edit Coverage Area</h1>");
            out.print("<table>");

            out.print("<tr><td><input type='hidden' name='Id' value='" + area.getId() + "'/></td></tr>");
            out.print("<tr><td><label for='area_name'>Area Name:</label></td><td><input type='text' name='area_name' value='" + area.getArea_name() + "' required/></td></tr>");
            out.print("<tr><td><label for='postal_code'>Postal Code:</label></td><td><input type='text' name='postal_code' value='" + area.getPostal_code() + "' required/></td></tr>");
            out.print("<tr><td colspan='2'><input type='submit' value='Edit & Save'/></td></tr>");

            out.print("</table>");
            out.println("</form>");
            
            out.println("<div style='text-align: center; margin-top: 15px;'>");
            out.println("<button onclick=\"window.history.back();\" style='padding: 10px 20px; background-color: #ccc; border: none; border-radius: 4px; cursor: pointer;'>Back</button>");
            out.println("</div>");
            
            out.println("</div>");
            out.println("</body>");

            out.println("</html>");
        } catch (Exception e) {
            System.out.println("Failed to process EditCoverageAreaServlet: " + e.getMessage());
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
        return "Edit Coverage Area Servlet";
    }
}
