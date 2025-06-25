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
      <td>
 <select name="status_${admin.person_id}" id="status_${admin.person_id}"
        onfocus="storeOldStatus(this)"
        onchange="handleStatusChange(this, '${admin.person_id}')">

        <c:forEach var="s" items="${['ACTIVE','INACTIVE','PENDING','REJECTED']}">
          <option value="${s}" <c:if test="${admin.status == s}">selected</c:if>>${s}</option>
        </c:forEach>
      </select>
    </td>
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
      body: JSON.stringify({ person_id: personId  , target_role : "ADMIN"})
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
    fetch('${pageContext.request.contextPath}/bank/admin', {
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
let oldStatusMap = {}; // Stores old values per person ID

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
                status: newStatus ,
               role : "ADMIN"
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
        alert("Error occurred: " + error);
        selectElement.value = oldStatus;
    }
}
</script>

</body>

</html>
