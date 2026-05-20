<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Contact Messages" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header"><div class="container"><h1>Contact Messages</h1></div></div>
<div class="container" style="padding:24px 20px;">
    <c:choose>
        <c:when test="${not empty messages}">
            <c:forEach var="msg" items="${messages}">
                <div class="card" style="${msg.read ? '' : 'border-left:4px solid var(--green);'}">
                    <div style="display:flex;justify-content:space-between;align-items:flex-start;flex-wrap:wrap;gap:10px;">
                        <div>
                            <strong>${msg.name}</strong>
                            <span class="text-muted" style="margin-left:8px;">&lt;${msg.email}&gt;</span>
                            <c:if test="${not msg.read}"><span class="status status-pending" style="margin-left:8px;">New</span></c:if>
                        </div>
                        <div class="text-muted" style="font-size:.82rem;">${msg.submittedAt}</div>
                    </div>
                    <div style="margin:8px 0;font-weight:600;">${msg.subject}</div>
                    <div class="text-muted" style="line-height:1.7;">${msg.message}</div>
                    <c:if test="${not msg.read}">
                        <form action="${pageContext.request.contextPath}/admin/messages/read" method="post" class="mt-1">
                            <input type="hidden" name="messageId" value="${msg.messageId}">
                            <button type="submit" class="btn btn-secondary btn-sm">Mark as Read</button>
                        </form>
                    </c:if>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info">No contact messages yet.</div>
        </c:otherwise>
    </c:choose>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


