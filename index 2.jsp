<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Book Store</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .navbar {
            margin-bottom: 20px;
        }
        .greeting {
            position: absolute; /* Đặt vị trí tuyệt đối */
            right: 20px; /* Căn lề bên phải */
            top: 10px; /* Căn lề bên trên */
            font-weight: bold; /* Chữ đậm */
        }
        .book-card {
            margin: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 15px;
            text-align: center;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">Book Store</a>
    <div class="collapse navbar-collapse">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="books?action=list">Books</a>
            </li>
            <c:choose>
                <c:when test="${not empty user}">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='logout.jsp'/>">Logout</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="nav-item">
                        <a class="nav-link" href="login.jsp">Login</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="register.jsp">Register</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>

<div class="container">
    <c:choose>
        <c:when test="${not empty user}">
            <c:if test="${user.role == 'ADMIN'}">
                <h2 class="text-center">Admin Dashboard</h2>
                <a href="addbook.jsp" class="btn btn-primary">Add Book</a>
                <div class="greeting">Welcome, ${user.username}!</div>
            </c:if>
            <c:if test="${user.role != 'ADMIN'}">
                <h2 class="text-center">Welcome to the Book Store</h2>
                <div class="greeting">Hello, ${user.username}!</div>
                <!-- Hiển thị sách cho người dùng thường -->
                <h3>Available Books</h3>
                <div class="row">
                    <c:forEach var="book" items="${books}">
                        <div class="col-md-4">
                            <div class="book-card">
                                <img src="<%= request.getContextPath() %>/<c:out value='${book.imagePath}' />" class="card-img-top" alt="${book.title}">
                                <h5 class="card-title">${book.title}</h5>
                                <p class="card-text">Author: ${book.author}</p>
                                <p class="card-text">Price: ${book.price} VND</p>
                                <c:if test="${not empty user}">
                                    <a href="payment.jsp?id=${book.id}" class="btn btn-success">Buy Now</a>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
        </c:when>
        <c:otherwise>
            <h2 class="text-center">Welcome to the Book Store</h2>
            <p>Please log in to view books and make purchases.</p>
            <!-- Hiển thị sách cho người dùng chưa đăng nhập -->
            <h3>Available Books</h3>
            <div class="row">
                <c:forEach var="book" items="${books}">
                    <div class="col-md-4">
                        <div class="book-card">
                            <img src="http://localhost:8080/cuahangsach_war_exploded/${book.imagePath}" class="card-img-top" alt="${book.title}">
                            <h5 class="card-title">${book.title}</h5>
                            <p class="card-text">Author: ${book.author}</p>
                            <p class="card-text">Price: ${book.price} VND</p>
                            <a href="login.jsp" class="btn btn-primary">Login to Buy</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<footer class="bg-light text-center py-3 mt-5">
    &copy; 2024 Trường Giang (VNPAY)
</footer>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
