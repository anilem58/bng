<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Login" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="auth-wrap">
    <div class="auth-card">
        <a href="${pageContext.request.contextPath}/" class="logo" style="justify-content:center;margin-bottom:20px;">
            <img src="${pageContext.request.contextPath}/assets/images/logo/bng-logo.png" alt="Logo" style="height:50px;">
        </a>
        <h2>Welcome Back</h2>

        <c:if test="${param.msg eq 'registered'}">
            <div class="alert alert-success">Registration successful! Your account is awaiting admin approval.</div>
        </c:if>
        <c:if test="${param.msg eq 'logged_out'}">
            <div class="alert alert-info">You have been logged out.</div>
        </c:if>
        <c:if test="${param.error eq 'login_required'}">
            <div class="alert alert-warning">Please log in to access that page.</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth" method="post" novalidate>
            <input type="hidden" name="action" value="login">
            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" class="form-control" required
                       value="${fn:escapeXml(param.email)}" autocomplete="email" placeholder="you@example.com">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" class="form-control" required autocomplete="current-password">
            </div>
            <div class="form-group" style="display:flex;align-items:center;gap:8px;">
                <input type="checkbox" id="rememberMe" name="rememberMe">
                <label for="rememberMe" style="margin-bottom:0;font-weight:400;">Remember me for 7 days</label>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Sign In</button>
        </form>
        <div class="auth-divider">Don't have an account?</div>
        <a href="${pageContext.request.contextPath}/auth?action=registerForm" class="btn btn-outline btn-block">Create Account</a>
    </div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


