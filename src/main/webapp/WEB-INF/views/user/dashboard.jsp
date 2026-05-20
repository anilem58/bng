<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="My Dashboard" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header">
    <div class="container">
        <h1>Hello, ${sessionScope.loggedInUser.fullName.split(' ')[0]}!</h1>
        <p class="text-muted">Welcome to your Bliss &amp; Glow account</p>
    </div>
</div>

<div class="container" style="padding:24px 20px;">
    <div class="layout-two-col">
        <!-- Sidebar -->
        <div class="sidebar">
            <div class="sidebar-title">My Account</div>
            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/user/dashboard" class="active">Dashboard</a>
                <a href="${pageContext.request.contextPath}/user/orders">My Orders</a>
                <a href="${pageContext.request.contextPath}/user/wishlist">Wishlist</a>
                <a href="${pageContext.request.contextPath}/user/profile">Profile Settings</a>
                <a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a>
            </nav>
        </div>

        <!-- Content -->
        <div>
            <!-- Stats -->
            <div class="stat-grid">
                <div class="stat-card">
                    <div class="stat-value">${recentOrders.size()}</div>
                    <div class="stat-label">Total Orders</div>
                </div>
                <div class="stat-card">
                    <a href="${pageContext.request.contextPath}/user/orders" style="text-decoration:none;color:inherit;">
                        <div class="stat-value">&#128722;</div>
                        <div class="stat-label">View Orders</div>
                    </a>
                </div>
            </div>

            <!-- Recent orders -->
            <div class="card">
                <div class="card-header"><h3>Recent Orders</h3></div>
                <c:choose>
                    <c:when test="${not empty recentOrders}">
                        <div class="table-wrap">
                            <table class="data-table">
                                <thead><tr><th>#</th><th>Date</th><th>Amount</th><th>Status</th><th></th></tr></thead>
                                <tbody>
                                    <c:forEach var="o" items="${recentOrders}" end="4">
                                        <tr>
                                            <td>#${o.orderId}</td>
                                            <td>${o.orderDate}</td>
                                            <td>NPR <fmt:formatNumber value="${o.totalAmount}" type="number" maxFractionDigits="2"/></td>
                                            <td><span class="status status-${o.status.toLowerCase()}">${o.status}</span></td>
                                            <td><a href="${pageContext.request.contextPath}/user/order?action=detail&id=${o.orderId}" class="btn btn-secondary btn-sm">View</a></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/user/orders" class="btn btn-outline btn-sm">View All Orders</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted">You haven't placed any orders yet. <a href="${pageContext.request.contextPath}/products">Start shopping!</a></p>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Quick links -->
            <div style="display:flex;gap:12px;flex-wrap:wrap;">
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Browse Products</a>
                <a href="${pageContext.request.contextPath}/user/wishlist" class="btn btn-outline">My Wishlist</a>
            </div>
        </div>
    </div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


