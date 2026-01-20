/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import com.DAO.laundryPackageDAO;
import com.Model.laundryPackage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class EditPackageServlet2 extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        System.out.println("Before TRY");

        try {
            String packageID = request.getParameter("packageID");
            String packageName = request.getParameter("packageName");
            String packageDesc = request.getParameter("packageDesc");
            Double packagePrice = Double.valueOf(request.getParameter("packagePrice"));

            // Handle image file upload
            Part part = request.getPart("packageImage");
            String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
            String packageImage;
            
            // Get current package to retain existing image if needed
            laundryPackage existingMenu = laundryPackageDAO.getPackageById(packageID);

            if (fileName != null && !fileName.isEmpty()) {
                // Use relative path based on webapp context
                String savePath = request.getServletContext().getRealPath("/packageImages");
                File fileSaveDir = new File(savePath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdirs();
                }

                // Save the uploaded file to /webapp/packageImages/
                part.write(savePath + File.separator + fileName);
                packageImage = fileName;
            } else {
                // No new file uploaded, keep existing image
                //laundryPackage existingMenu = laundryPackageDAO.getPackageById(packageID);
                packageImage = existingMenu.getPackageImage();
            }

            // Prepare updated package data
            laundryPackage pack = new laundryPackage();
            pack.setPackageID(packageID);
            pack.setPackageName(packageName);
            pack.setPackageDesc(packageDesc);
            pack.setPackagePrice(packagePrice);
            pack.setPackageImage(packageImage);

            // Update in database
            int status = laundryPackageDAO.update(pack);

            if (status > 0) {
                out.print("<script>alert('Record updated successfully!');</script>");
                request.getRequestDispatcher("managePackage.jsp").include(request, response);
            } else {
                out.print("<script>alert('Sorry! Unable to update record.');</script>");
                request.getRequestDispatcher("managePackage.jsp").include(request, response);
            }

        } catch (Exception e) {
            out.print("<script>alert('Error: " + e.getMessage() + "');</script>");
            request.getRequestDispatcher("managePackage.jsp").include(request, response);
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
        return "EditPackageServlet2 - Updates laundry package info with optional image upload.";
    }
}
