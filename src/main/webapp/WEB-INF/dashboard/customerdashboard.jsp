<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FINTRUST | DASHBOARD</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/customerdashboard.css">
</head>
<body>

<jsp:include page="dashboardheader.jsp" />

<nav id="dashboard-menu" class="dashboard-navbar">
    <ul>
        <li>FINTRUST</li>
        <li><a href="${pageContext.request.contextPath}/bank/customer/dashboard">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/allaccounts">My Accounts</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/transaction">Transfer Money</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/history">Account Statement</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/customer/additional-account">Open New Account</a></li>
    </ul>
</nav>

<c:set var="pageAttr" value="${requestScope.page != null ? requestScope.page : 'home'}" />
<c:choose>
    <c:when test="${pageAttr == 'home'}">
        <jsp:include page="home.jsp" />
    </c:when>
    <c:when test="${pageAttr == 'myaccounts'}">
        <jsp:include page="myaccounts.jsp" />
    </c:when>
    <c:when test="${pageAttr == 'additional-account'}">
        <jsp:include page="additional-account.jsp" />
    </c:when>
    <c:when test="${pageAttr == 'transaction'}">
        <jsp:include page="transaction/transaction.jsp" />
    </c:when>
     <c:when test="${pageAttr == 'history'}">
        <jsp:include page="accountstatement.jsp" />
    </c:when>
    <c:otherwise>
        <jsp:include page="notfound.jsp" />
    </c:otherwise>
</c:choose>

</body>
</html>
