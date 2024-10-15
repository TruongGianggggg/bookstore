<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.cuahangsach.model.Book" %>
<%@ page import="org.example.cuahangsach.dao.BookDAO" %>
<%
    int bookId = Integer.parseInt(request.getParameter("id"));
    BookDAO bookDao = new BookDAO();
    Book book = bookDao.getBookById(bookId);
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sửa Sách - Bán Sách</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<header class="bg-dark text-white text-center py-3">
    <h1>Sửa Thông Tin Sách</h1>
</header>

<main class="container mt-4">
    <form action="updateBook" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="<%= book.getId() %>">

        <div class="form-group">
            <label for="title">Tên Sách:</label>
            <input type="text" id="title" name="title" class="form-control" value="<%= book.getTitle() %>" required>
        </div>

        <div class="form-group">
            <label for="author">Tác Giả:</label>
            <input type="text" id="author" name="author" class="form-control" value="<%= book.getAuthor() %>" required>
        </div>

        <div class="form-group">
            <label for="price">Giá (VND):</label>
            <input type="number" id="price" name="price" class="form-control" value="<%= book.getPrice() %>" required>
        </div>

        <div class="form-group">
            <label for="imagePath">Đường dẫn Hình Ảnh:</label>
            <input type="text" id="imagePath" name="imagePath" class="form-control" value="<%= book.getImagePath() %>" required>
        </div>

        <button type="submit" class="btn btn-primary">Cập Nhật Sách</button>
        <a href="index.jsp" class="btn btn-secondary">Hủy</a>
    </form>
</main>

<footer class="bg-dark text-white text-center py-3">
    <p>© 2024 Trường Giang(VNPAY)</p>
</footer>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
