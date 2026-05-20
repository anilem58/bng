<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Create Account" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="auth-wrap">
    <div class="auth-card" style="max-width:520px;">
        <h2>Create Your Account</h2>
        <p class="text-muted text-center mb-2">Your account will be active after admin approval.</p>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth" method="post" novalidate>
            <input type="hidden" name="action" value="register">
            <div class="form-group">
                <label for="fullName">Full Name *</label>
                <input type="text" id="fullName" name="fullName" class="form-control" required
                       value="${fn:escapeXml(user.fullName)}" placeholder="Jane Doe">
            </div>
            <div class="form-group">
                <label for="email">Email Address *</label>
                <input type="email" id="email" name="email" class="form-control" required
                       value="${fn:escapeXml(user.email)}" placeholder="jane@example.com" autocomplete="email">
            </div>
            <div class="form-group">
                <label for="phone">Phone Number *</label>
                <input type="tel" id="phone" name="phone" class="form-control" required
                       value="${fn:escapeXml(user.phone)}" placeholder="9800000000">
            </div>
            <div class="form-group">
                <label for="dob">Date of Birth</label>
                <input type="date" id="dob" name="dob" class="form-control">
            </div>
            <div class="form-group">
                <label for="address">Address</label>
                <input type="text" id="address" name="address" class="form-control"
                       value="${fn:escapeXml(user.address)}" placeholder="Street, City, Country">
            </div>
            <div class="form-group">
                <label for="password">Password *</label>
                <input type="password" id="password" name="password" class="form-control" required autocomplete="new-password">
                <small id="passwordHint" class="text-muted"></small>
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirm Password *</label>
                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
                <small id="confirmHint" class="text-muted"></small>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Register</button>
        </form>
        <div class="auth-divider">Already have an account?</div>
        <a href="${pageContext.request.contextPath}/auth?action=loginForm" class="btn btn-outline btn-block">Sign In</a>
    </div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


