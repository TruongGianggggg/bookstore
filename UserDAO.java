package org.example.cuahangsach.dao;

import org.example.cuahangsach.enums.UserRole;
import org.example.cuahangsach.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection getConnection() {
        // Kết nối tới cơ sở dữ liệu
        String jdbcURL = "jdbc:mysql://localhost:3306/bookstore";
        String jdbcUsername = "root";
        String jdbcPassword = "Gianglq2004&";

        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void registerUser(User user) {
        String sql = "INSERT INTO bookstore.user (username, password, role) VALUES (?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().getRole()); // Lưu vai trò dưới dạng chuỗi
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User loginUser(String username, String password) {
        String sql = "SELECT * FROM bookstore.user WHERE username = ? AND password = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        UserRole.valueOf(resultSet.getString("role").toUpperCase()) // Chuyển đổi từ chuỗi về enum
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Kiểm tra sự tồn tại của tài khoản admin
    private User getAdminUser() {
        String sql = "SELECT * FROM bookstore.user WHERE role = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, UserRole.ADMIN.getRole());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        UserRole.valueOf(resultSet.getString("role").toUpperCase())
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức tạo tài khoản admin mặc định
    public void createDefaultAdmin() {
        if (getAdminUser() == null) { // Kiểm tra xem admin đã tồn tại
            User admin = new User("hathanhthao", "3042004", UserRole.ADMIN);
            registerUser(admin);
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM bookstore.user WHERE username = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        UserRole.valueOf(resultSet.getString("role").toUpperCase()) // Chuyển đổi từ chuỗi về enum
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Nếu không tìm thấy người dùng
    }

    // Lấy danh sách tất cả người dùng
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM bookstore.user";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        UserRole.valueOf(resultSet.getString("role").toUpperCase())
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Cập nhật thông tin người dùng
    public void updateUser(String username, User user) {
        String sql = "UPDATE bookstore.user SET password = ?, role = ? WHERE username = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getRole().getRole());
            statement.setString(3, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa người dùng
    public void deleteUser(String username) {
        String sql = "DELETE FROM bookstore.user WHERE username = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
