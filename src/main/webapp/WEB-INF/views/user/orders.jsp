<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="My Orders" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header"><div class="container"><h1>My Orders</h1></div></div>
<div class="container" style="padding:24px 20px;">
<div class="layout-two-col">
    <div class="sidebar">
        <div class="sidebar-title">My Account</div>
        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/user/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/user/orders" class="active">My Orders</a>
            <a href="${pageContext.request.contextPath}/user/wishlist">Wishlist</a>
            <a href="${pageContext.request.contextPath}/user/profile">Profile Settings</a>
            <a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a>
        </nav>
    </div>
    <div>
        <c:if test="${param.msg eq 'cancelled'}"><div class="alert alert-info alert-auto">Order cancelled successfully.</div></c:if>
        <div class="card">
            <div class="card-header"><h3>Order History</h3></div>
            <c:choose>
                <c:when test="${not empty orders}">
                    <div class="table-wrap">
                        <table class="data-table">
                            <thead><tr><th>Order #</th><th>Date</th><th>Total</th><th>Status</th><th>Actions</th></tr></thead>
                            <tbody>
                                <c:forEach var="o" items="${orders}">
                                    <tr>
                                        <td>#${o.orderId}</td>
                                        <td><fmt:formatDate value="${o.orderDate}" pattern="dd MMM yyyy" /></td>
                                        <td>NPR <fmt:formatNumber value="${o.totalAmount}" type="number" maxFractionDigits="2"/></td>
                                        <td><span class="status status-${o.status.toLowerCase()}">${o.status}</span></td>
                                        <td style="display:flex;gap:6px;flex-wrap:wrap;">
                                            <a href="${pageContext.request.contextPath}/user/order?action=detail&id=${o.orderId}" class="btn btn-secondary btn-sm">View</a>
                                            <c:if test="${o.status eq 'PENDING'}">
                                                <form action="${pageContext.request.contextPath}/user/order" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="cancel">
                                                    <input type="hidden" name="orderId" value="${o.orderId}">
                                                    <button type="submit" class="btn btn-danger btn-sm btn-confirm-delete">Cancel</button>
                                                </form>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">No orders yet. <a href="${pageContext.request.contextPath}/products">Start shopping!</a></p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


