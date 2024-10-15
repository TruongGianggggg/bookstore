package org.example.cuahangsach.controller;

import org.example.cuahangsach.dao.PaymentDAO;
import org.example.cuahangsach.model.Payment;
import org.example.cuahangsach.service.VNPAYService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    private VNPAYService vnPayService = new VNPAYService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Hiển thị trang thanh toán
        request.getRequestDispatcher("payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        double amount = Double.parseDouble(request.getParameter("amount"));
        String paymentMethod = request.getParameter("paymentMethod");

        // Kiểm tra phương thức thanh toán
        if ("vnpay".equalsIgnoreCase(paymentMethod)) {
            // Tạo URL thanh toán VNPay
            String ipAddress = request.getRemoteAddr();
            String paymentUrl = vnPayService.createPaymentUrl((long) amount, ipAddress);

            // Chuyển hướng người dùng đến URL của VNPay
            response.sendRedirect(paymentUrl);
        } else {
            // Xử lý các phương thức thanh toán khác (nếu có)
            // Tạo đối tượng Payment
            Payment payment = new Payment(username, amount, paymentMethod);

            // Gọi phương thức lưu thanh toán trong PaymentDAO
            PaymentDAO paymentDAO = new PaymentDAO();
            boolean isSuccess = paymentDAO.savePayment(payment);

            // Lưu thông báo vào request
            if (isSuccess) {
                request.setAttribute("message", "Thanh toán thành công!");
            } else {
                request.setAttribute("message", "Thanh toán không thành công! Vui lòng thử lại.");
            }

            // Chuyển hướng lại đến trang thanh toán
            request.getRequestDispatcher("payment.jsp").forward(request, response);
        }
    }
}
