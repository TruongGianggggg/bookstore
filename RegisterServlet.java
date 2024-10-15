package org.example.cuahangsach.controller;


import org.example.cuahangsach.dao.UserDAO;
import org.example.cuahangsach.enums.UserRole;
import org.example.cuahangsach.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDao;

    @Override
    public void init() throws ServletException {
        super.init();
        userDao = new UserDAO(); // Khởi tạo UserDao
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chuyển hướng đến trang đăng ký nếu có yêu cầu GET
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Nhận thông tin từ form đăng ký
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Kiểm tra xem tên người dùng đã tồn tại chưa
        User existingUser = userDao.loginUser(username, password);
        if (existingUser != null) {
            request.setAttribute("errorMessage", "Tên tài khoản đã tồn tại. Vui lòng chọn tên khác.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Tạo đối tượng người dùng mới với vai trò USER
        User newUser = new User(username, password, UserRole.USER);

        // Lưu thông tin người dùng vào cơ sở dữ liệu
        userDao.registerUser(newUser);

        // Chuyển hướng đến trang đăng nhập
        response.sendRedirect("login.jsp");
    }
}
