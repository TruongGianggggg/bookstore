package org.example.cuahangsach.dao;

import org.example.cuahangsach.model.Voucher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {

    // Kết nối tới cơ sở dữ liệu
    private static Connection getConnection() {
        String jdbcURL = "jdbc:mysql://localhost:3306/bookstore";
        String jdbcUsername = "root";
        String jdbcPassword = "Dang972004@";

        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Thêm voucher mới
    public static void addVoucher(Voucher voucher) {
        String sql = "INSERT INTO voucher (code, title, discountPercentage, expiryDate, isActive, imagePath) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, voucher.getCode());
            statement.setString(2, voucher.getTitle());
            statement.setDouble(3, voucher.getDiscountPercentage());
            statement.setString(4, voucher.getExpiryDate());
            statement.setBoolean(5, voucher.getIsActive());
            statement.setString(6, voucher.getImagePath()); // Lưu tên file ảnh hoặc đường dẫn ảnh
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateVoucher(Voucher voucher) {
        String sql = "UPDATE voucher SET code = ?, title = ?, discountPercentage = ?, expiryDate = ?, isActive = ?, imagePath = ? WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, voucher.getCode());
            statement.setString(2, voucher.getTitle());
            statement.setDouble(3, voucher.getDiscountPercentage());
            statement.setString(4, voucher.getExpiryDate());
            statement.setBoolean(5, voucher.getIsActive());
            statement.setString(6, voucher.getImagePath());
            statement.setLong(7, voucher.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Voucher getVoucherById(Long id) {
        String sql = "SELECT * FROM voucher WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Voucher(
                        resultSet.getLong("id"),
                        resultSet.getString("code"),
                        resultSet.getString("title"),
                        resultSet.getDouble("discountPercentage"),
                        resultSet.getString("expiryDate"),
                        resultSet.getBoolean("isActive"),
                        resultSet.getString("imagePath")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<Voucher> getAllVouchers() {
        List<Voucher> vouchers = new ArrayList<>();
        String sql = "SELECT * FROM bookstore.voucher";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Voucher voucher = new Voucher(
                        resultSet.getLong("id"),
                        resultSet.getString("code"),
                        resultSet.getString("title"),
                        resultSet.getDouble("discountPercentage"),
                        resultSet.getString("expiryDate"),
                        resultSet.getBoolean("isActive"),
                        resultSet.getString("imagePath")
                );
                System.out.println(voucher);
                vouchers.add(voucher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(vouchers.size());
        return vouchers;
    }

    public static void deleteVoucher(Long id) {
        String sql = "DELETE FROM voucher WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

