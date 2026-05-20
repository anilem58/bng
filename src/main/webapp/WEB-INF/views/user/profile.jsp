<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="My Profile" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header"><div class="container"><h1>My Profile</h1></div></div>
<div class="container" style="padding:24px 20px;">
<div class="layout-two-col">
    <div class="sidebar">
        <div class="sidebar-title">My Account</div>
        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/user/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/user/orders">My Orders</a>
            <a href="${pageContext.request.contextPath}/user/wishlist">Wishlist</a>
            <a href="${pageContext.request.contextPath}/user/profile" class="active">Profile Settings</a>
            <a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a>
        </nav>
    </div>
    <div>
        <c:if test="${param.msg eq 'updated'}"><div class="alert alert-success alert-auto">Profile updated successfully.</div></c:if>
        <c:if test="${param.msg eq 'pwd_changed'}"><div class="alert alert-success alert-auto">Password changed successfully.</div></c:if>

        <!-- Profile update -->
        <div class="card mb-3">
            <div class="card-header"><h3>Personal Information</h3></div>
            <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
            <form action="${pageContext.request.contextPath}/user/profile" method="post" novalidate>
                <div class="form-group">
                    <label>Full Name *</label>
                    <input type="text" name="fullName" class="form-control" required value="${fn:escapeXml(profileUser.fullName)}">
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" class="form-control" value="${fn:escapeXml(profileUser.email)}" readonly style="background:var(--green-pale);">
                </div>
                <div class="form-group">
                    <label>Phone *</label>
                    <input type="tel" name="phone" class="form-control" required value="${fn:escapeXml(profileUser.phone)}">
                </div>
                <div class="form-group">
                    <label>Date of Birth</label>
                    <input type="date" name="dob" class="form-control" value="${profileUser.dob}">
                </div>
                <div class="form-group">
                    <label>Address</label>
                    <input type="text" name="address" class="form-control" value="${fn:escapeXml(profileUser.address)}">
                </div>
                <button type="submit" class="btn btn-primary">Update Profile</button>
            </form>
        </div>

        <!-- Change password -->
        <div class="card">
            <div class="card-header"><h3>Change Password</h3></div>
            <c:if test="${not empty pwdError}"><div class="alert alert-danger">${pwdError}</div></c:if>
            <form action="${pageContext.request.contextPath}/user/changePassword" method="post" novalidate>
                <div class="form-group">
                    <label>Current Password *</label>
                    <input type="password" name="currentPassword" class="form-control" required autocomplete="current-password">
                </div>
                <div class="form-group">
                    <label>New Password *</label>
                    <input type="password" id="password" name="newPassword" class="form-control" required autocomplete="new-password">
                    <small id="passwordHint" class="text-muted"></small>
                </div>
                <div class="form-group">
                    <label>Confirm New Password *</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
                    <small id="confirmHint" class="text-muted"></small>
                </div>
                <button type="submit" class="btn btn-primary">Change Password</button>
            </form>
        </div>
    </div>
</div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


