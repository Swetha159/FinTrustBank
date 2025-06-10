<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/header/header.css">
</head>
<header>
  <div class="header-container">
    <div class="bank-container">
      <img src="<%= request.getContextPath() %>/assets/ftlogowhite.png" alt="logo" class="logo">
      <div class="bank-text">
        <div class="bank-name">FINTRUST</div>
        <div class="bank-tagline">Your Perfect Banking Partner</div>
      </div>
    </div>

    <div class="support-links">
      <a href="#">About</a>
      <a href="#">Help</a>
      <a href="#">FAQ</a>
      <a href="#">Complaint</a>
    </div>
<form action="login" method="get">
  <button type="submit" class="login-button">Login</button>
</form>

  </div>
</header>
</html>
