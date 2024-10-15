package org.example.cuahangsach.controller;


import org.example.cuahangsach.dao.VoucherDAO;
import org.example.cuahangsach.model.Voucher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/vouchers")
@MultipartConfig
public class VoucherServlet extends HttpServlet {
    private List<Voucher> vouchers = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null || action.equals("list")) {
            vouchers = VoucherDAO.getAllVouchers();
            request.setAttribute("vouchers", vouchers);
            RequestDispatcher dispatcher = request.getRequestDispatcher("voucher_list.jsp");
            dispatcher.forward(request, response);
        } else if (action.equals("new")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("voucher_form.jsp");
            dispatcher.forward(request, response);
        } else if (action.equals("edit")) {
            Long id = Long.parseLong(request.getParameter("id"));
            Voucher voucher = VoucherDAO.getVoucherById(id);
            request.setAttribute("voucher", voucher);
            RequestDispatcher dispatcher = request.getRequestDispatcher("voucher_form.jsp");
            dispatcher.forward(request, response);
        } else if (action.equals("delete")) {
            Long id = Long.parseLong(request.getParameter("id"));
            VoucherDAO.deleteVoucher(id);
            response.sendRedirect("vouchers?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String code = request.getParameter("code");
        String title = request.getParameter("title");
        Double discountPercentage = Double.parseDouble(request.getParameter("discountPercentage"));
        String expiryDate = request.getParameter("expiryDate");
        Boolean isActive = request.getParameter("isActive") != null;

        // Xử lý file ảnh
        Part filePart = request.getPart("image");
        String fileName = filePart.getSubmittedFileName();
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir(); // Tạo thư mục uploads nếu chưa có
        }

        // Lưu file ảnh vào thư mục uploads
        if (fileName != null && !fileName.isEmpty()) {
            filePart.write(uploadPath + File.separator + fileName);
        }
        String imagePath = "uploads/" + fileName;

        if (id == null || id.isEmpty()) {
            // Create new voucher
            Long newId = (long) (vouchers.size() + 1);
            Voucher newVoucher = new Voucher(newId, code, title, discountPercentage, expiryDate, isActive, imagePath);
            vouchers.add(newVoucher);
            VoucherDAO.addVoucher(newVoucher);
        } else {
            // Update voucher
            Long voucherId = Long.parseLong(id);
            for (Voucher v : vouchers) {
                if (v.getId().equals(voucherId)) {
                    v.setCode(code);
                    v.setDiscountPercentage(discountPercentage);
                    v.setExpiryDate(expiryDate);
                    v.setIsActive(isActive);
                    if (!fileName.isEmpty()) {
                        v.setImagePath(imagePath);  // Cập nhật đường dẫn ảnh nếu có file ảnh mới
                    }
                    VoucherDAO.updateVoucher(v);
                    break;
                }
            }
        }

        response.sendRedirect("vouchers?action=list");
    }
}

