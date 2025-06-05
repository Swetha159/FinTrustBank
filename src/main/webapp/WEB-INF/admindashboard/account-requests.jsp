<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>Account Requests</title>
<link rel = "stylesheet" href="${pageContext.request.contextPath}/css/account-requests.css">
</head>
<body>
  <h2>Account Requests</h2>

<c:if test="${not empty param.alertMessage}">
  <c:set var="alertMessage" value="${param.alertMessage}"  scope="request"/>
  <jsp:include page="/WEB-INF/views/alert.jsp" />
</c:if>


 
  <table>
    <tr>
      <th>Name</th>
      <th>Email</th>
      <th>Phone Number</th>
      <th>DOB</th>
      <th>Aadhar</th>
      <th>PAN</th>
      <th>Address</th>
      <th>Account Type</th>
      <th>Created At</th>
      <th>Modified By</th>
      <th>Action</th>
    </tr>

    <c:choose>
      <c:when test="${empty requests}">
        <tr>
          <td colspan="11" style="text-align: center; font-style: italic; color: gray;">
            No account request found.
          </td>
        </tr>
      </c:when>
      <c:otherwise>
        <c:forEach var="request" items="${requests}">
          <tr>
            <td>${request.name}</td>
            <td>${request.email}</td>
            <td>${request.phone_number}</td>
            <td>${request.dob}</td>
            <td>${request.aadhar}</td>
            <td>${request.pan}</td>
            <td>${request.address}</td>
            <td>${request.account_type}</td>
            <td>${request.created_at}</td>
            <td>
              <c:choose>
                <c:when test="${request.person_id == request.modified_by}">
                  <span class="self-request">Self Request</span>
                </c:when>
                <c:otherwise>
                  <a href="viewModifierDetails?modifierId=${request.modified_by}">${request.modifier_name}</a>
                </c:otherwise>
              </c:choose>
            </td>
            <td>
              <button onclick="approveAccount('${request.person_id}', '${request.account_type}')">Accept</button>
                <button onclick="rejectAccount('${request.person_id}')">Reject</button>
         
            </td>
          </tr>
        </c:forEach>
      </c:otherwise>
    </c:choose>
  </table>

  <script>
    function approveAccount(personId, accountType) {
      fetch('${pageContext.request.contextPath}/bank/account-request/approval', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          person_id: personId,
          account_type: accountType,
        })
      }).then(response => response.text())
        .then(msg => {
          window.location.href = '${pageContext.request.contextPath}/bank/admin/requests?alertMessage=' + encodeURIComponent(msg);
        })
        .catch(err => {
          console.error("Error approving account:", err);
          window.location.href = '${pageContext.request.contextPath}/bank/admin/requests?alertMessage=' + encodeURIComponent("Error accepting the request.");
        });
    }
    function rejectAccount(personId) {
        fetch('${pageContext.request.contextPath}/bank/account-request/rejection', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            person_id: personId
          })
        }).then(response => response.text())
          .then(msg => {
            window.location.href = '${pageContext.request.contextPath}/bank/admin/requests?alertMessage=' + encodeURIComponent("Account Request Rejected");
          })
          .catch(err => {
            console.error("Error approving account:", err);
            window.location.href = '${pageContext.request.contextPath}/bank/admin/requests?alertMessage=' + encodeURIComponent("Error rejecting the request.");
          });
      }
  </script>

</body>
</html>
