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
                    <input type="text" id="email" name="email" required>
                    <label for="email">Email</label>
                </div>
            </div>

            <div class="row">
                <div class="input-group">
                    <input type="text" id="dob" name="dob" required>
                    <label for="dob">Date of Birth(DD/MM/YYY)</label>
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
                    <select name="role" id="role" required>
                        <option value="">Select Role</option>
                        <option value="ADMIN">Admin</option>
                        <option value="SUPERADMIN">Super Admin</option>
                    </select>
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
        const name = document.getElementById("name").value.trim();
        const email = document.getElementById("email").value.trim();
     
        const phone = document.getElementById("phone").value.trim();
        const address = document.getElementById("address").value.trim();
        const aadhar = document.getElementById("aadhar").value.trim();
        const pan = document.getElementById("pan").value.trim();
        const role = document.getElementById("role").value;
        const branchId = document.getElementById("branchId").value;
        const dobInput = document.getElementById("dob");
        const dob = dobInput.value.trim();
        
        const dobPattern = /^(0[1-9]|[12][0-9]|3[01])[\/](0[1-9]|1[0-2])[\/](19|20)\d\d$/;
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const phonePattern = /^[6-9]\d{9}$/;
        const aadharPattern = /^\d{12}$/;
        const panPattern = /^[A-Z]{5}[0-9]{4}[A-Z]{1}$/;

        if (name === "" || email === "" || dob === "" || phone === "" || address === "" || aadhar === "" || pan === "" || role === "" || branchId === "") {
            alert("Please fill in all fields.");
            return;
        }

        if (!emailPattern.test(email)) {
            alert("Please enter a valid email.");
            return;
        }

        if (!phonePattern.test(phone)) {
            alert("Please enter a valid 10-digit phone number.");
            return;
        }

        if (!aadharPattern.test(aadhar)) {
            alert("Aadhar must be a 12-digit number.");
            return;
        }

        if (!panPattern.test(pan)) {
            alert("PAN must be in format: ABCDE1234F");
            return;
        }
        if (!dobPattern.test(dob)) {
            alert("Date of Birth must be in dd/mm/yyyy format.");
            dobInput.focus();
            return;
        }
        const data = {
            name: name,
            email: email,
            dob: dob,
            phone_number: phone,
            address: address,
            aadhar: aadhar,
            pan: pan,
            role: role,
            branch_id: branchId
        };

        const redirectUrl = role === "ADMIN"
            ? '${pageContext.request.contextPath}/bank/admins'
            : '${pageContext.request.contextPath}/bank/superadmins';

        fetch('${pageContext.request.contextPath}/bank/admin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => {
            if (response.ok) {
                alert(role + " created successfully!");
                window.location.href = redirectUrl;
            } else {
                alert("Error: Failed to create " + role.toLowerCase());
            }
        })
        .catch(error => {
            alert("Error: " + error.message);
        });
    }
</script>
</body>
</html>
