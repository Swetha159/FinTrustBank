<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Admins</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/account-requests.css">
</head>
<body>

<c:if test="${not empty param.alertMessage}">
  <c:set var="alertMessage" value="${param.alertMessage}" scope="request" />
  <jsp:include page="/WEB-INF/views/alert.jsp" />
</c:if>

<h2>Admin List</h2>

<button type="button" onclick="openCreateAdmin()">Create Admin</button>

<table>
  <thead>
    <tr>
      <th>Person ID</th>
      <th>Name</th>
      <th>Email</th>
      <th>Phone Number</th>
      <th>Status</th>
      <th>Update</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="admin" items="${admins}">
      <tr>
        <td>${admin.person_id}</td>
        <td>${admin.name}</td>
        <td>${admin.email}</td>
        <td>${admin.phone_number}</td>
        <td>${admin.status}</td>
        <td>
         <button type="button" onclick="submitAdminDetails('${admin.person_id}')">Edit</button>


        </td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<script>
function submitAdminDetails(personId) {
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
    }) 
    .then(html => {
      document.open();
      document.write(html);
      document.close();
    })
    .catch(error => {
      console.error("Error submitting admin details:", error);
    });
}
function openCreateAdmin() {
    fetch('${pageContext.request.contextPath}/bank/branch', {
        method: 'GET'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to load admin creation page.");
        }
        return response.text();
    })
    .then(html => {
        document.open();  // Clear current page
        document.write(html); // Render new page from server
        document.close();
    })
    .catch(error => {
        console.error("Error loading create admin page:", error);
        alert("Unable to open Create Admin page.");
    });
}
</script>

</body>

</html>
