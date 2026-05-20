<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Page Not Found" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="auth-wrap">
    <div style="text-align:center;">
        <div style="font-size:5rem;margin-bottom:16px;">404</div>
        <h2 style="color:var(--green-dark);">Page Not Found</h2>
        <p class="text-muted mt-1">Sorry, the page you're looking for doesn't exist.</p>
        <div class="mt-3 d-flex gap-2 justify-center" style="justify-content:center;">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go Home</a>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline">Shop Products</a>
        </div>
    </div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


