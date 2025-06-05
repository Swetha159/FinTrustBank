<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DASHBOARD</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/superadmindashboard.css">
</head>
<body>

<jsp:include page="../dashboard/dashboardheader.jsp" />

<nav id="dashboard-menu" class="dashboard-navbar">
    <ul>
        <li>FINTRUST</li>
        <li><a href="${pageContext.request.contextPath}/bank/superadmin/dashboard">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/admins">Admins</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/branches">Branches</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/admin/requests">Account Requests</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/customers">Customers</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/transaction">Transfer Money</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/history">Account Statement</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/admin/additional-account">Additional Account Creation</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/account-request">New Account Creation</a></li>
    </ul>
</nav>


<c:set var="pageAttr" value="${requestScope.page != null ? requestScope.page : 'home'}" />
<c:choose>
<c:when test="${pageAttr == 'home'}">
        <jsp:include page="home.jsp" />
    </c:when>
    <c:when test="${pageAttr == 'requests'}">
        <jsp:include page="account-requests.jsp" />
    </c:when>
        <c:when test="${pageAttr == 'branches'}">
        <jsp:include page="branches.jsp" />
    </c:when>
    <c:when test="${pageAttr == 'customers'}">
        <jsp:include page="customers.jsp" />
    </c:when>
    <c:when test="${pageAttr == 'additional-account'}">
        <jsp:include page="../dashboard/additional-account.jsp" />
    </c:when>
    <c:when test="${pageAttr == 'transaction'}">
        <jsp:include page="../dashboard/transaction/transaction.jsp" />
    </c:when>
     <c:when test="${pageAttr == 'history'}">
        <jsp:include page="../dashboard/accountstatement.jsp" />
    </c:when>
     <c:when test="${pageAttr == 'admins'}">
        <jsp:include page="../admindashboard/admins.jsp" />
    </c:when>
    <c:otherwise>
        <jsp:include page="notfound.jsp" />
    </c:otherwise>
</c:choose>


</body>
</html>