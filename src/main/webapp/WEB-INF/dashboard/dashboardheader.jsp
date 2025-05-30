<!-- dashboardheader.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboardHeader.css">

</head>
<body>
<header class="dashboard-header">
  <div class="left-section">
    <img src="${pageContext.request.contextPath}/assets/ftwhitelogo.png" alt="FinTrust Logo" class="logo">
    <button id="menu-toggle" class="menu-btn" title="Menu">&#9776;</button>
    <h4>DASHBOARD</h4>
  </div>

  <div class="right-section">
    <img src="${pageContext.request.contextPath}/assets/email.png" alt="Email" class="icon">
    <div class="profile">
      <img src="${pageContext.request.contextPath}/assets/person.jpg" alt="Profile" class="profile-pic">
      <div class="user-info">
        <p class="user-name">${sessionScope.name}</p>
        <p class="user-id">${sessionScope.userId}</p>
      </div>
    </div>
  </div>
</header>
 <script>
  document.addEventListener('DOMContentLoaded', function () {
    const menuToggle = document.getElementById('menu-toggle');
    const navMenu = document.getElementById('dashboard-menu');
    const header = document.querySelector('.dashboard-header');

    menuToggle.addEventListener('click', function () {
      navMenu.classList.toggle('visible');
      header.classList.toggle('shifted');
    });
  });
</script>

</body>
</html>
