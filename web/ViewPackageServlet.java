/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.DAO.laundryPackageDAO;
import com.Model.laundryPackage;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author M S I
 */
@WebServlet("/ViewPackageServlet")
public class ViewPackageServlet extends HttpServlet {
    
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    List<laundryPackage> packages = null;

    try {
        packages = laundryPackageDAO.getAllLaundryPackage();
    } catch (ClassNotFoundException e) {
        e.printStackTrace(); // Log for debugging
        request.setAttribute("error", "Unable to load laundry packages.");
    }

    request.setAttribute("laundryPackages", packages);
    RequestDispatcher dispatcher = request.getRequestDispatcher("menuLaundry.jsp");
    dispatcher.forward(request, response);
}
}
