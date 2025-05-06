<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>FinTrust Bank</title>
</head>
<body>
<div class="error-container">
    <h1>Error</h1>
    <p>${errorMessage != null ? errorMessage : "An unexpected error occurred."}</p>
    <br>
    <a href="${pageContext.request.contextPath}/">Return to Home</a>
</div>
</body>
</html>
