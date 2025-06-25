<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Dashboard Header</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboardHeader.css">
</head>
<body>
<header class="dashboard-header">
  <div class="left-section">
    <div class="logo-text">FT</div>
    <button id="menu-toggle" class="menu-btn" title="Menu">&#9776;</button>
    <h4>DASHBOARD</h4>
  </div>

  <div class="right-section">
   
        <div class="profile">
    <form action="${pageContext.request.contextPath}/bank/profile" method="get" style="margin: 0;">
      <button type="submit" class="profile-icon" title="Profile">&#128100;</button>
    </form>
    <div class="user-info">
      <p class="user-name">${name}</p>
      <p class="user-id">${personId}</p>
    </div> 
  </div>
      
    <form action="${pageContext.request.contextPath}/bank/logout" method="post" style="display:inline;">
      <button type="submit" class="logout-btn" title="Logout">Logout</button>
    </form>
  </div>
</header>

<script>
  document.addEventListener('DOMContentLoaded', function () {
    const menuToggle = document.getElementById('menu-toggle');
    const navMenu = document.getElementById('dashboard-menu');
    const header = document.querySelector('.dashboard-header');

    menuToggle.addEventListener('click', function () {
      navMenu?.classList.toggle('visible');
      header?.classList.toggle('shifted');
    });
  });
</script>

</body>
</html>
