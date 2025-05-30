<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DASHBOARD</title>
</head>
<body>
<jsp:include page="dashboardheader.jsp"></jsp:include>

<nav class="dashboard-navbar">
  <ul>
    <li><a href="${pageContext.request.contextPath}/bank/customer/dashboard">Home</a></li>
    <li><a href="${pageContext.request.contextPath}/bank/customer/details">Profile</a></li>
    <li><a href="${pageContext.request.contextPath}/bank/transactions">Transactions</a></li>
    <li><a href="${pageContext.request.contextPath}/bank/settings">Settings</a></li>
    <li><a href="${pageContext.request.contextPath}/bank/logout">Logout</a></li>
  </ul>
</nav>

</body>
</html>