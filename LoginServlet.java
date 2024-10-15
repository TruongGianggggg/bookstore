package org.example.cuahangsach.controller;

import org.example.cuahangsach.dao.UserDAO;
import org.example.cuahangsach.enums.UserRole;
import org.example.cuahangsach.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDAO.loginUser(username, password); // Đổi tên phương thức cho đúng với tên phương thức
        if (user != null) {
            // Đăng nhập thành công
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Chuyển hướng dựa trên vai trò
            if (user.getRole() == UserRole.ADMIN) {
                response.sendRedirect("admin_dashboard.jsp"); // Chuyển hướng đến trang admin
            } else {
                response.sendRedirect("index.jsp");
            }
        } else {
            // Đăng nhập thất bại
            request.setAttribute("errorMessage", "Tài khoản hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
