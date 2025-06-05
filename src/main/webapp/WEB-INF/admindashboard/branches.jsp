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
                <td>${branch.name}</td>
                <td>${branch.created_at}</td>
                <td>${branch.modified_at}</td>
                <td>${branch.modifier_name}</td>
                <td>
                    <button type="button" onclick="editBranch('${branch.branch_id}')">Edit</button>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<script>
function editBranch(branchId) {
    fetch('${pageContext.request.contextPath}/bank/branchdetails', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ branch_id: branchId })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to load branch details.");
        }
        return response.text();
    })
    .then(html => {
        document.open();
        document.write(html);
        document.close();
    })
    .catch(error => {
        console.error("Error editing branch:", error);
        alert("Unable to edit branch.");
    });
}

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
</script>

</body>
</html>
