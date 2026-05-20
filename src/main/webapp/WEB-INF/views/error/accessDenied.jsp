<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Access Denied" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="auth-wrap">
    <div style="text-align:center;">
        <div style="font-size:5rem;margin-bottom:16px;">🔒</div>
        <h2 style="color:var(--green-dark);">Access Denied</h2>
        <p class="text-muted mt-1">You don't have permission to access that page.</p>
        <div class="mt-3" style="display:flex;gap:12px;justify-content:center;">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go Home</a>
            <a href="${pageContext.request.contextPath}/auth?action=loginForm" class="btn btn-outline">Login</a>
        </div>
    </div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


