package org.example.cuahangsach.dao;

import org.example.cuahangsach.model.Payment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentDAO {

    private Connection getConnection() {
        // Kết nối tới cơ sở dữ liệu
        String jdbcURL = "jdbc:mysql://localhost:3306/bookstore"; // Thay đổi theo cấu hình của bạn
        String jdbcUsername = "root"; // Thay đổi theo cấu hình của bạn
        String jdbcPassword = "Gianglq2004&"; // Thay đổi theo cấu hình của bạn

        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Phương thức lưu thông tin thanh toán
    public boolean savePayment(Payment payment) {
        String sql = "INSERT INTO bookstore.payments (username, amount, paymentMethod) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, payment.getUsername());
            statement.setDouble(2, payment.getAmount());
            statement.setString(3, payment.getPaymentMethod());
            statement.executeUpdate();
            return true; // Trả về true nếu lưu thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }
}
