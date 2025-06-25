<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Assign Manager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create-branch.css">
    <style>
        .hidden { display: none; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Assign Manager to Branch</h2>

<input type="hidden" id="branchId" name="branchId" value="${branch_id}" />

<div class="input-group">
    <label for="location">Branch Location:</label>
    <input type="text" id="location" name="location" value="${location}" readonly />
</div>

        <div class="button-group">
            <button type="button" onclick="redirectToCreateAdmin()">Create New Super Admin</button>
            <button type="button" onclick="showExistingAdminForm()">Add Existing Admin</button>
        </div>

        <form id="assignManagerForm" class="hidden">
            <div class="input-group">
                <label for="managerId">Existing Manager Person ID:</label>
                <input type="text" id="managerId" name="managerId" required />
            </div>
            <div class="button-group">
                <button type="submit">Assign Manager</button>
            </div>
        </form>
    </div>

<script>
    const form = document.getElementById("assignManagerForm");

    function redirectToCreateAdmin() {
        window.location.href = '${pageContext.request.contextPath}/bank/admin';
    }

    function showExistingAdminForm() {
        form.classList.remove("hidden");
    }

    form.addEventListener("submit", function (e) {
        e.preventDefault();

        const branchId = document.getElementById("branchId").value;
        const managerId = document.getElementById("managerId").value.trim();

        if (!managerId) {
            alert("Please enter a valid Manager ID.");
            return;
        }

        fetch(`${pageContext.request.contextPath}/bank/person/role`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ person_id: managerId })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch role.");
            }
            return response.json();
        })
        .then(data => {
            const role = data.role;

            if (role === "SUPERADMIN") {
                const confirmProceed = confirm(
                    "This person is already a Superadmin. Assigning them will affect their existing branch association. Do you want to proceed?"
                );
                if (!confirmProceed) return;
            }

            const assignData = {
                branch_id: branchId,
                manager_id: managerId,
                role: role
            };

            return fetch('${pageContext.request.contextPath}/bank/branch/superadmin', {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(assignData)
            });
        })
        .then(response => {
            if (response && response.ok) {
                alert("Manager assigned successfully!");
                window.location.href = '${pageContext.request.contextPath}/bank/branches';
            } else if (response) {
                alert("Failed to assign manager.");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("An error occurred while checking role or assigning manager.");
        });
    });
</script>

</body>
</html>
