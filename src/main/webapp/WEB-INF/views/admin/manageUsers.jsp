<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Manage Users" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header"><div class="container"><h1>Manage Users</h1></div></div>
<div class="container" style="padding:24px 20px;">
    <c:if test="${param.msg eq 'approved'}"><div class="alert alert-success alert-auto">User approved.</div></c:if>
    <c:if test="${param.msg eq 'rejected'}"><div class="alert alert-warning alert-auto">User rejected.</div></c:if>

    <div class="card">
        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Phone</th><th>Role</th><th>Status</th><th>Joined</th><th>Actions</th></tr></thead>
                <tbody>
                    <c:forEach var="u" items="${users}">
                        <tr>
                            <td>${u.userId}</td>
                            <td>${u.fullName}</td>
                            <td>${u.email}</td>
                            <td>${u.phone}</td>
                            <td>${u.role}</td>
                            <td><span class="status status-${u.status.toLowerCase()}">${u.status}</span></td>
                            <td>${u.createdAt}</td>
                            <td>
                                <c:if test="${u.status eq 'PENDING' and u.role eq 'CUSTOMER'}">
                                    <form action="${pageContext.request.contextPath}/admin/users/approve" method="post" style="display:inline;">
                                        <input type="hidden" name="userId" value="${u.userId}">
                                        <button type="submit" class="btn btn-primary btn-sm">Approve</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin/users/reject" method="post" style="display:inline;">
                                        <input type="hidden" name="userId" value="${u.userId}">
                                        <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


