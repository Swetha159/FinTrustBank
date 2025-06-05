<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create-admin.css">
</head>
<body>
<div class="page-wrapper">
    <h2>Create Admin</h2>

    <form id="adminForm" onsubmit="event.preventDefault(); createAdmin();">
        <div class="container">

            <div class="row">
                <div class="input-group">
                    <input type="text" id="name" name="name" required>
                    <label for="name">Name</label>
                </div>
                <div class="input-group">
                    <input type="email" id="email" name="email" required>
                    <label for="email">Email</label>
                </div>
            </div>

            <div class="row">
                <div class="input-group">
                    <input type="text" id="dob" name="dob" required>
                    <label for="dob">Date of Birth</label>
                </div>
                <div class="input-group">
                    <input type="text" id="phone" name="phone" required>
                    <label for="phone">Phone Number</label>
                </div>
            </div>

            <div class="row">
                <div class="input-group">
                    <input type="text" id="address" name="address" required>
                    <label for="address">Address</label>
                </div>
            </div>

            <div class="row">
                <div class="input-group">
                    <input type="text" id="aadhar" name="aadhar" required>
                    <label for="aadhar">Aadhar</label>
                </div>
                <div class="input-group">
                    <input type="text" id="pan" name="pan" required>
                    <label for="pan">PAN</label>
                </div>
            </div>

            <div class="row">
                <div class="input-group">
                    <select name="branchId" id="branchId" required>
                        <option value="">Select Branch</option>
                        <c:forEach var="branch" items="${branches}">
                            <option value="${branch.branchId}">${branch.location} (${branch.branchId})</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="nav-buttons">
                <button type="submit" class="submit-btn">Submit</button>
            </div>
        </div>
    </form>
</div>

<script>
    function createAdmin() {
        const data = {
            name: document.getElementById("name").value,
            email: document.getElementById("email").value,
            dob: document.getElementById("dob").value,
            phone_number: document.getElementById("phone").value,
            address: document.getElementById("address").value,
            aadhar: document.getElementById("aadhar").value,
            pan: document.getElementById("pan").value,
            branch_id: document.getElementById("branchId").value
        };

        fetch('${pageContext.request.contextPath}/bank/admin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => {
            if (response.ok) {
                alert("Admin created successfully!");
                window.location.href = '${pageContext.request.contextPath}/bank/admins'; 
            } else {
                return response.text().then(msg => { throw new Error(msg); });
            }
        })
        .catch(error => {
            alert("Error: " + error.message);
        });
    }
</script>
</body>
</html>
