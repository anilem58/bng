<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Server Error" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="auth-wrap">
    <div style="text-align:center;">
        <div style="font-size:5rem;margin-bottom:16px;">500</div>
        <h2 style="color:var(--green-dark);">Something Went Wrong</h2>
        <p class="text-muted mt-1">We're sorry — an internal error occurred. Please try again later.</p>
        <div class="mt-3" style="display:flex;gap:12px;justify-content:center;">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go Home</a>
        </div>
    </div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


