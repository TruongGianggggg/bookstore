package org.example.cuahangsach.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/uploads/*")
public class ImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy tên file từ URL
        String fileName = request.getPathInfo().substring(1);
        String filePath = getServletContext().getRealPath("") + File.separator + "uploads" + File.separator + fileName;

        File file = new File(filePath);
        if (file.exists()) {
            response.setContentType(getServletContext().getMimeType(file.getName()));
            response.setContentLength((int) file.length());

            // Đọc file và gửi về client
            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            in.close();
            out.close();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // Trả về 404 nếu không tìm thấy file
        }
    }
}
