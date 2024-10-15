package org.example.cuahangsach.controller;

import org.example.cuahangsach.dao.BookDAO;
import org.example.cuahangsach.model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/books")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class BookControllerServlet extends HttpServlet {
    private BookDAO bookDAO;

    @Override
    public void init() {
        bookDAO = new BookDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteBook(request, response);
                break;
            default:
                listBooks(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        System.out.println("Action: "+action);
        switch (action) {
            case "insert":
                insertBook(request, response);
                break;
            case "update":
                updateBook(request, response);
                break;
            default:
                listBooks(request, response);
                break;
        }
    }

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private void insertBook(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        double price = Double.parseDouble(request.getParameter("price"));

        // Xử lý file ảnh
        Part filePart = request.getPart("image"); // Lấy phần hình ảnh
        String fileName = filePart.getSubmittedFileName();

        // Đường dẫn đến thư mục tải lên
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);

        // Tạo thư mục nếu chưa tồn tại
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Tạo file để lưu hình ảnh
        File file = new File(uploadDir, fileName);
        // Lưu file hình ảnh
        filePart.write(file.getAbsolutePath());

        // Lưu thông tin sách vào database (sử dụng đường dẫn hình ảnh)
        String imagePath = "uploads/" + fileName; // Đường dẫn hình ảnh trong server

        Book newBook = new Book(title, author, price, imagePath);
        bookDAO.addBook(newBook);
        request.setAttribute("message", "Thêm sách thành công!");

        // Chuyển hướng về trang index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("addBook.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Book existingBook = bookDAO.getBookById(id);
        request.setAttribute("book", existingBook);
        request.getRequestDispatcher("editBook.jsp").forward(request, response);
    }

    private void updateBook(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        double price = Double.parseDouble(request.getParameter("price"));

        // Nhập đường dẫn ảnh từ người dùng
        String imagePath = request.getParameter("imagePath");

        // Kiểm tra nếu đường dẫn không rỗng
        if (imagePath == null || imagePath.isEmpty()) {
            request.setAttribute("message", "Vui lòng nhập đường dẫn ảnh hợp lệ!");
        } else {
            Book book = new Book(id, title, author, price, imagePath);
            bookDAO.updateBook(book);
        }

        response.sendRedirect("books?action=list");
    }

    private void deleteBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        bookDAO.deleteBook(id);
        response.sendRedirect("books?action=list");
    }

    private void listBooks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            System.out.println(book.getImagePath());
            System.out.println(book.getTitle());
        }
        request.setAttribute("books", books);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
