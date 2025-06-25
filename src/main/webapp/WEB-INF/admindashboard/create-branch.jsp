<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Create Branch</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create-branch.css">
</head>
<body>
    <div class="container">
        <h2>Create New Branch</h2>
        <form id="createBranchForm">
            <div class="input-group">
                <label for="location">Branch Location:</label>
                <input type="text" id="location" name="location" required />
            </div>


            <div class="button-group">
                <button type="submit">Create Branch</button>
            </div>
        </form>
    </div>

    <script>
        document.getElementById("createBranchForm").addEventListener("submit", function (e) {
            e.preventDefault();

            const location = document.getElementById("location").value;
       
            const data = {
                location: location 
               
            };

            fetch('${pageContext.request.contextPath}/bank/branch', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            })
            .then(response => {
                if (response.ok) {
                    alert("Branch created successfully!");
                    window.location.href = '${pageContext.request.contextPath}/bank/branches';
                } else {
                    alert("Branch creation failed.");
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("Error occurred while creating branch.");
            });
        });
    </script>
</body>
</html>
