<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/header/header.css">
</head>
<body>
<header class="main-header">
  <div class="header-content">
    
    <div class="brand">
      <div class="logo">FT</div>
      <div class="bank-info">
        <div class="bank-name">FINTRUST</div>
        <div class="tagline">Your Perfect Banking Partner</div>
      </div>
    </div>

    <nav class="nav-links">
      <a href="#">About</a>
      <a href="#">Help</a>
      <a href="#">FAQ</a>
      <a href="#">Complaint</a>
    </nav>

    <form action="login" method="get">
      <button type="submit" class="login-btn">Login</button>
    </form>

  </div>
</header>
</body>
</html>
