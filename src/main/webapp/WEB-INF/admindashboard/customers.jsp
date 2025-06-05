<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Customers</title>
<link rel = "stylesheet" href="${pageContext.request.contextPath}/css/account-requests.css">
</head>
<body>
<c:if test="${not empty param.alertMessage}">
  <c:set var="alertMessage" value="${param.alertMessage}"  scope="request"/>
  <jsp:include page="/WEB-INF/views/alert.jsp" />
</c:if>
<h2>Customer Account List</h2>

<table>
  <thead>
    <tr>
      <th>Person ID</th>
      <th>Name</th>
      <th>Email</th>
      <th>Phone Number</th>
      <th>Account Number</th>
      <th>Balance</th>
      <th>Status</th>
      <th>Action</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="cust" items="${customers}">
      <tr>
        <td>${cust.person_id}</td>
        <td>${cust.name}</td>
        <td>${cust.email}</td>
        <td>${cust.phone_number}</td>
        <td>${cust.account_no}</td>
        <td>${cust.balance}</td>
        <td>${cust.status}</td>
        <td>
         <button type="button" onclick="submitCustomerDetails('${cust.person_id}')">Edit</button>


        </td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<script>
function submitCustomerDetails(personId) {
    fetch('${pageContext.request.contextPath}/bank/person/details', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ person_id: personId })
    })
    .then(response => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.text(); 
    .then(html => {
  
      document.open();
      document.write(html);
      document.close();
    })
    .catch(error => {
      console.error("Error submitting customer details:", error);
    });
}
</script>
</body>
</html>











