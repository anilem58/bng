<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Reports" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header"><div class="container"><h1>Reports &amp; Analytics</h1></div></div>
<div class="container" style="padding:24px 20px;">

    <!-- Revenue summary -->
    <div class="stat-grid">
        <div class="stat-card">
            <div class="stat-value">NPR <fmt:formatNumber value="${totalRevenue}" type="number" maxFractionDigits="0"/></div>
            <div class="stat-label">Total Revenue (Delivered)</div>
        </div>
    </div>

    <!-- Top 5 Products -->
    <div class="card mb-3">
        <div class="card-header"><h3>Top 5 Most-Ordered Products</h3></div>
        <c:choose>
            <c:when test="${not empty topProducts}">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead><tr><th>#</th><th>Product</th><th>Units Sold</th><th>Revenue (NPR)</th></tr></thead>
                        <tbody>
                            <c:forEach var="row" items="${topProducts}" varStatus="s">
                                <tr>
                                    <td>${s.index + 1}</td>
                                    <td>${row[0]}</td>
                                    <td>${row[1]}</td>
                                    <td><fmt:formatNumber value="${row[2]}" type="number" maxFractionDigits="2"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise><p class="text-muted">No order data yet.</p></c:otherwise>
        </c:choose>
    </div>

    <!-- Sales by Category -->
    <div class="card mb-3">
        <div class="card-header"><h3>Sales by Category</h3></div>
        <c:choose>
            <c:when test="${not empty salesByCategory}">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead><tr><th>Category</th><th>Units Sold</th><th>Revenue (NPR)</th></tr></thead>
                        <tbody>
                            <c:forEach var="row" items="${salesByCategory}">
                                <tr>
                                    <td>${row[0]}</td>
                                    <td>${row[2]}</td>
                                    <td><fmt:formatNumber value="${row[1]}" type="number" maxFractionDigits="2"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise><p class="text-muted">No sales data yet.</p></c:otherwise>
        </c:choose>
    </div>

    <!-- Stock vs Ordered -->
    <div class="card">
        <div class="card-header"><h3>Stock vs Orders Comparison</h3></div>
        <c:choose>
            <c:when test="${not empty stockReport}">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead><tr><th>Product</th><th>SKU</th><th>Current Stock</th><th>Total Ordered</th><th>Status</th></tr></thead>
                        <tbody>
                            <c:forEach var="row" items="${stockReport}">
                                <tr>
                                    <td>${row[0]}</td>
                                    <td><small>${row[1]}</small></td>
                                    <td>${row[2]}</td>
                                    <td>${row[3]}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${row[2] == 0}"><span class="status status-cancelled">Out of Stock</span></c:when>
                                            <c:when test="${row[2] < 10}"><span class="status status-pending">Low Stock</span></c:when>
                                            <c:otherwise><span class="status status-approved">In Stock</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise><p class="text-muted">No product data.</p></c:otherwise>
        </c:choose>
    </div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


