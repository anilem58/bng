<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Manage Orders" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header"><div class="container"><h1>Manage Orders</h1></div></div>
<div class="container" style="padding:24px 20px;">
    <c:if test="${param.msg eq 'updated'}"><div class="alert alert-success alert-auto">Order status updated.</div></c:if>
    <div class="card">
        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>#</th><th>Customer</th><th>Date</th><th>Total</th><th>Status</th><th>Actions</th></tr></thead>
                <tbody>
                    <c:forEach var="o" items="${orders}">
                        <tr>
                            <td>#${o.orderId}</td>
                            <td>${o.customerName}</td>
                            <td>${o.orderDate}</td>
                            <td>NPR <fmt:formatNumber value="${o.totalAmount}" type="number" maxFractionDigits="2"/></td>
                            <td><span class="status status-${o.status.toLowerCase()}">${o.status}</span></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/orders/detail?id=${o.orderId}" class="btn btn-secondary btn-sm">View</a>
                                <form action="${pageContext.request.contextPath}/admin/orders/status" method="post" style="display:inline;gap:4px;">
                                    <input type="hidden" name="orderId" value="${o.orderId}">
                                    <select name="status" class="form-control" style="display:inline-block;width:auto;padding:5px 8px;font-size:.82rem;">
                                        <option value="PENDING"   ${o.status eq 'PENDING'   ? 'selected' : ''}>Pending</option>
                                        <option value="CONFIRMED" ${o.status eq 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                                        <option value="SHIPPED"   ${o.status eq 'SHIPPED'   ? 'selected' : ''}>Shipped</option>
                                        <option value="DELIVERED" ${o.status eq 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                        <option value="CANCELLED" ${o.status eq 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                                    </select>
                                    <button type="submit" class="btn btn-primary btn-sm">Update</button>
                                </form>
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


