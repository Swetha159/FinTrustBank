<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Branches</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/account-requests.css">
</head>
<body>

<c:if test="${not empty param.alertMessage}">
    <c:set var="alertMessage" value="${param.alertMessage}" scope="request" />
    <jsp:include page="/WEB-INF/views/alert.jsp" />
</c:if>

<h2>Branch List</h2>

<button type="button" onclick="openCreateBranch()">Create Branch</button>

<table>
    <thead>
        <tr>
            <th>Branch ID</th>
            <th>Location</th>
            <th>IFSC Code</th>
            <th>Manager</th>
            <th>Created At</th>
            <th>Modified At</th>
            <th>Modified By</th>
            <th>Edit</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="branch" items="${branches}">
            <tr>
                <td>${branch.branch_id}</td>
                <td>${branch.location}</td>
                <td>${branch.ifsc_code}</td>
       <td>
    <c:choose>
        <c:when test="${empty branch.name}">
            Not yet assigned
        </c:when>
        <c:otherwise>
            ${branch.name}
        </c:otherwise>
    </c:choose>
</td>
<td>${branch.created_at}</td>
<td>${branch.modified_at}</td>
<td>${branch.modifier_name}</td>
<td>
    <c:choose>
        <c:when test="${empty branch.name}">
            <button type="button" onclick="assignManager('${branch.branch_id}' , '${branch.location }')">Add Manager</button>
        </c:when>
        <c:otherwise>
            <button type="button" onclick="assignManager('${branch.branch_id}' , '${branch.location }')">Change Manager</button>
        </c:otherwise>
    </c:choose>
</td>

            </tr>
        </c:forEach>
    </tbody>
</table>

<script>
function openCreateBranch() {
    fetch('${pageContext.request.contextPath}/bank/branch', {
        method: 'GET'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to load create branch page.");
        }
        return response.text();
    })
    .then(html => {
        document.open();
        document.write(html);
        document.close();
    })
    .catch(error => {
        console.error("Error loading create branch page:", error);
        alert("Unable to open Create Branch page.");
    });
}

function assignManager(branchId, location) {
    const data = {
        branch_id: branchId,
        location: location
    };

    fetch('${pageContext.request.contextPath}/bank/branch/superadmin', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) throw new Error("Failed to load assign manager page.");
        return response.text();
    })
    .then(html => {
        document.open();
        document.write(html);
        document.close();
    })
    .catch(error => {
        console.error("Error loading assign manager page:", error);
        alert("Unable to open Assign Manager page.");
    });
}

</script>

</body>
</html>
