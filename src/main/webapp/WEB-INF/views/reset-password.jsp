<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Reset Password</title></head>
<body>
<h2>Reset Your Password</h2>

<form id="resetForm" method="POST">
    <input type="password" id="newPassword" placeholder="New Password" required />
    <button type="submit">Reset Password</button>
</form>

<script>
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

    document.getElementById('resetForm').onsubmit = async (e) => {
        e.preventDefault();
        const newPassword = document.getElementById('newPassword').value;

        const response = await fetch("${pageContext.request.contextPath}/bank/reset-password", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ token, newPassword })
        });

        const data = await response.json();
        alert(data.message || data.error);
        if (response.ok) window.location.href = '/bank/login';
    };
</script>

</body>
</html>
