package org.example.cuahangsach.controller;

import org.example.cuahangsach.dao.UserDAO;
import org.example.cuahangsach.model.User;
import org.example.cuahangsach.enums.UserRole;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
        userDAO.createDefaultAdmin(); // Tạo tài khoản admin nếu chưa tồn tại
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("list".equals(action)) {
            listUsers(request, response);
        } else if ("edit".equals(action)) {
            editUser(request, response);
        } else if ("delete".equals(action)) {
            deleteUser(request, response);
        } else {
            response.sendRedirect("users?action=list");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createUser(request, response);
        } else if ("update".equals(action)) {
            updateUser(request, response);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("user.jsp").forward(request, response);
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        User newUser = new User(username, password, UserRole.valueOf(role.toUpperCase()));
        userDAO.registerUser(newUser);
        response.sendRedirect("users?action=list");
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        User user = userDAO.getUserByUsername(username);
        request.setAttribute("user", user);
        request.getRequestDispatcher("user.jsp").forward(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        User updatedUser = new User(username, password, UserRole.valueOf(role.toUpperCase()));
        userDAO.updateUser(username, updatedUser);
        response.sendRedirect("users?action=list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        userDAO.deleteUser(username);
        response.sendRedirect("users?action=list");
    }
}

