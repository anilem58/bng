<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Admin Dashboard" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header">
    <div class="container">
        <h1>Admin Dashboard</h1>
        <p class="text-muted">Welcome back, ${sessionScope.loggedInUser.fullName}</p>
    </div>
</div>
<div class="container" style="padding:24px 20px;">

    <!-- Stats -->
    <div class="stat-grid">
        <div class="stat-card">
            <div class="stat-value">${totalUsers}</div>
            <div class="stat-label">Total Customers</div>
        </div>
        <div class="stat-card" style="border-left-color:#dc3545;">
            <div class="stat-value" style="color:#dc3545;">${pendingUsers}</div>
            <div class="stat-label">Pending Approvals</div>
        </div>
        <div class="stat-card">
            <div class="stat-value">${totalProducts}</div>
            <div class="stat-label">Products</div>
        </div>
        <div class="stat-card">
            <div class="stat-value">${totalOrders}</div>
            <div class="stat-label">Total Orders</div>
        </div>
        <div class="stat-card" style="border-left-color:#856404;">
            <div class="stat-value" style="color:#856404;">${unreadMessages}</div>
            <div class="stat-label">Unread Messages</div>
        </div>
    </div>

    <!-- Quick nav -->
    <div style="display:flex;gap:12px;flex-wrap:wrap;margin-bottom:28px;">
        <a href="${pageContext.request.contextPath}/admin/products/add" class="btn btn-primary">+ Add Product</a>
        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline">Manage Users</a>
        <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-outline">Manage Orders</a>
        <a href="${pageContext.request.contextPath}/admin/reports" class="btn btn-outline">Reports</a>
    </div>

    <!-- Recent orders -->
    <div class="card">
        <div class="card-header"><h3>Recent Orders</h3></div>
        <c:choose>
            <c:when test="${not empty recentOrders}">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead><tr><th>#</th><th>Customer</th><th>Date</th><th>Amount</th><th>Status</th><th></th></tr></thead>
                        <tbody>
                            <c:forEach var="o" items="${recentOrders}" end="9">
                                <tr>
                                    <td>#${o.orderId}</td>
                                    <td>${o.customerName}</td>
                                    <td>${o.orderDate}</td>
                                    <td>NPR <fmt:formatNumber value="${o.totalAmount}" type="number" maxFractionDigits="2"/></td>
                                    <td><span class="status status-${o.status.toLowerCase()}">${o.status}</span></td>
                                    <td><a href="${pageContext.request.contextPath}/admin/orders/detail?id=${o.orderId}" class="btn btn-secondary btn-sm">View</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise><p class="text-muted">No orders yet.</p></c:otherwise>
        </c:choose>
    </div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


