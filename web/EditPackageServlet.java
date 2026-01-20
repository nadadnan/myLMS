/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.DAO.laundryPackageDAO;
import com.Model.laundryPackage;
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
public class EditPackageServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("Edit Menu Servlet");
        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Update Laundry Package</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<style>");
            out.println("<%@include file='headerAdmin.jsp'%>");
            out.println("body { background-color: #f4f4f4; margin: 0; padding: 0; }");
            out.println(".container { max-width: 600px; margin: 50px auto; padding: 20px; background-color: #fff; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); border-radius: 8px; }");
            out.println("h1 { text-align: center; color: #333; }");
            out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            out.println("td { padding: 10px; vertical-align: top; }");
            out.println("input[type='text'], textarea { width: calc(100% - 22px); padding: 10px; margin: 5px 0; border: 1px solid #ccc; border-radius: 4px; }");
            out.println("input[type='file'] { padding: 10px; margin: 5px 0; }");
            out.println("input[type='submit'] { width: 100%; padding: 15px; background-color: #27ae60; color: #fff; border: none; border-radius: 4px; font-size: 16px; cursor: pointer; }");
            out.println("input[type='submit']:hover { background-color: #219150; }");
            out.println("label { display: block; margin-bottom: 5px; font-weight: bold; }");
            out.println(".radio-group { display: flex; gap: 20px; }");
            out.println(".radio-group input { width: auto; margin-right: 5px; }");

            out.println("@media (max-width: 768px) {");
            out.println("  table, tbody, tr, td { display: block; width: 100%; }");
            out.println("  td { padding: 10px 0; }");
            out.println("  .radio-group { flex-direction: column; gap: 10px; }");
            out.println("  input[type='submit'] { font-size: 14px; padding: 12px; }");
            out.println("}");

            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            // Retrieve menu ID from request parameter
            String sid = request.getParameter("packageID");
            int id = 0;
            try {
                id = Integer.parseInt(sid);
                System.out.println("Convert Success Id:" + id);
            } catch (Exception e) {
                System.out.println("Fail To convert");
            }

            System.out.println("Before retrieving from database");
            // Retrieve menu details from database
            laundryPackage e = laundryPackageDAO.getPackageById(sid);
            System.out.println("Success Retrieve from database " + e.getPackageName());
            out.println("<div class='container'>");
            out.println("<form action='EditPackageServlet2' method='post' enctype='multipart/form-data'>");
            out.println("<h1>Edit Laundry Package</h1>");
            out.print("<table>");

            out.print("<tr><td><input type='hidden' name='packageID' value='" + e.getPackageID() + "'/></td></tr>");
            out.print("<tr><td><label for='packageName'>Name:</label></td><td><input type='text' name='packageName' value='" + e.getPackageName() + "' required/></td></tr>");
            out.print("<tr><td><label for='packageDesc'>Description:</label></td><td><textarea name='packageDesc' rows='4' cols='50' required>" + e.getPackageDesc() + "</textarea></td></tr>");
            out.print("<tr><td><label for='packagePrice'>Price:</label></td><td><input type='text' name='packagePrice' value='" + e.getPackagePrice() + "' required/></td></tr>");
            out.print("<tr><td><label for='packageImage'>Current Image:</label></td><td><img src='packageImages/" + e.getPackageImage() + "' width='100' height='100'/></td></tr>");

            out.print("<tr><td><label for='packageImage'>New Image:</label></td><td><input type='file' name='packageImage'/></td></tr>");
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
            System.out.println("FAILLL");
        }
        //out.close();
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
