<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit Branch</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create-branch.css">
</head>
<body>

<div class="container">
    <h2>Edit Branch</h2>

    <form id="editBranchForm">
        <div class="input-group">
            <label for="branchId">Branch ID</label>
            <input type="text" id="branchId" name="branchId" value="${branch.branch_id}" readonly>
        </div>

        <div class="input-group">
            <label for="location">Location</label>
            <input type="text" id="location" name="location" value="${branch.location}" required>
        </div>

        <div class="input-group">
            <label for="managerId">Manager ID</label>
            <input type="text" id="managerId" name="managerId" value="${branch.manager_id}" required>
        </div>

        <div class="button-group">
            <button type="submit">Update Branch</button>
        </div>
    </form>
</div>

<script>
document.getElementById('editBranchForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const data = {
        branch_id: document.getElementById('branchId').value,
        location: document.getElementById('location').value,
        manager_id: document.getElementById('managerId').value
    };

    fetch('${pageContext.request.contextPath}/bank/branch', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (response.ok) {
            alert('Branch updated successfully!');
            window.location.href = '${pageContext.request.contextPath}/bank/branches';
        } else {
            alert('Failed to update branch.');
        }
    })
    .catch(error => {
        console.error('Error updating branch:', error);
        alert('Server error occurred.');
    });
});
</script>

</body>
</html>
