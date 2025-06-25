<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Super Admins</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/account-requests.css">
</head>
<body>

<c:if test="${not empty param.alertMessage}">
  <c:set var="alertMessage" value="${param.alertMessage}" scope="request" />
  <jsp:include page="/WEB-INF/views/alert.jsp" />
</c:if>

<h2>Super Admin List</h2>

<button type="button" onclick="openCreateSuperAdmin()">Create Super Admin</button>

<table>
  <thead>
    <tr>
      <th>Person ID</th>
      <th>Name</th>
      <th>Email</th>
      <th>Phone Number</th>
       <th>Branch Id</th>
        <th>Location</th>
      <th>Status</th>
      <th>Update</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="superadmin" items="${superadmins}">
      <tr>
        <td>${superadmin.person_id}</td>
        <td>${superadmin.name}</td>
        <td>${superadmin.email}</td>
        <td>${superadmin.phone_number}</td>
            <td>${superadmin.branch_id}</td>
                <td>${superadmin.location}</td>
        <td>
          <select name="status_${superadmin.person_id}" id="status_${superadmin.person_id}"
                  onfocus="storeOldStatus(this)"
                  onchange="handleStatusChange(this, '${superadmin.person_id}')">
            <c:forEach var="s" items="${['ACTIVE','INACTIVE','PENDING','REJECTED']}">
              <option value="${s}" <c:if test="${superadmin.status == s}">selected</c:if>>${s}</option>
            </c:forEach>
          </select>
        </td>
        <td>
          <button type="button" onclick="submitSuperAdminDetails('${superadmin.person_id}')">Edit</button>
        </td>
      </tr>
    </c:forEach>
  </tbody>
</table>

<script>
function submitSuperAdminDetails(personId) {
    fetch('${pageContext.request.contextPath}/bank/person/details', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ person_id: personId, target_role: "SUPERADMIN" })
    })
    .then(response => {
      if (!response.ok) throw new Error("Network error");
      return response.text();
    })
    .then(html => {
      document.open();
      document.write(html);
      document.close();
    })
    .catch(error => {
      console.error("Error submitting superadmin details:", error);
    });
}

function openCreateSuperAdmin() {
    fetch('${pageContext.request.contextPath}/bank/admin', {
        method: 'GET'
    })
    .then(response => {
        if (!response.ok) throw new Error("Load error");
        return response.text();
    })
    .then(html => {
        document.open();
        document.write(html);
        document.close();
    })
    .catch(error => {
        console.error("Error loading create superadmin page:", error);
        alert("Unable to open Create Super Admin page.");
    });
}

let oldStatusMap = {};

function storeOldStatus(selectElement) {
    oldStatusMap[selectElement.id] = selectElement.value;
}

async function handleStatusChange(selectElement, personId) {
    const newStatus = selectElement.value;
    const oldStatus = oldStatusMap[selectElement.id];

    if (newStatus === oldStatus) return;

    const message = [
  	  "Do you want to change the status from",
  	  oldStatus,
  	  "to",
  	  newStatus + "?"
  	].join(" ");
  	const confirmChange = confirm(message);

  if (!confirmChange) {
      selectElement.value = oldStatus;
      return;
  }

    try {
        const response = await fetch('${pageContext.request.contextPath}/bank/person/status', {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                person_id: personId,
                status: newStatus,
                role: "SUPERADMIN"
            })
        });

        if (response.ok) {
            alert("Status updated successfully.");
            oldStatusMap[selectElement.id] = newStatus;
        } else {
            alert("Failed to update status.");
            selectElement.value = oldStatus;
        }
    } catch (error) {
        alert("Error: " + error);
        selectElement.value = oldStatus;
    }
}
</script>

</body>
</html>
