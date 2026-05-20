<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Order #${order.orderId}" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header"><div class="container"><h1>Order #${order.orderId}</h1></div></div>
<div class="container" style="padding:24px 20px;max-width:800px;">
    <div class="card mb-2">
        <div class="card-header"><h3>Order Info</h3></div>
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:14px;">
            <div><strong>Customer:</strong><br>${order.customerName}</div>
            <div><strong>Status:</strong><br><span class="status status-${order.status.toLowerCase()}">${order.status}</span></div>
            <div><strong>Date:</strong><br>${order.orderDate}</div>
            <div><strong>Shipping Address:</strong><br>${order.shippingAddress}</div>
            <div><strong>Total:</strong><br>
                <span style="font-size:1.2rem;font-weight:700;color:var(--green);">NPR <fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="2"/></span>
            </div>
        </div>
        <div class="mt-2">
            <form action="${pageContext.request.contextPath}/admin/orders/status" method="post" style="display:flex;gap:10px;align-items:center;flex-wrap:wrap;">
                <input type="hidden" name="orderId" value="${order.orderId}">
                <select name="status" class="form-control" style="width:auto;">
                    <option value="PENDING"   ${order.status eq 'PENDING'   ? 'selected' : ''}>Pending</option>
                    <option value="CONFIRMED" ${order.status eq 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                    <option value="SHIPPED"   ${order.status eq 'SHIPPED'   ? 'selected' : ''}>Shipped</option>
                    <option value="DELIVERED" ${order.status eq 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                    <option value="CANCELLED" ${order.status eq 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                </select>
                <button type="submit" class="btn btn-primary">Update Status</button>
            </form>
        </div>
    </div>
    <div class="card">
        <div class="card-header"><h3>Items</h3></div>
        <c:forEach var="item" items="${order.items}">
            <div class="cart-item">
                <img class="cart-item-img" src="${pageContext.request.contextPath}${item.imagePath}" alt="${item.productName}">
                <div style="flex:1;">
                    <div class="cart-item-name">${item.productName}</div>
                    <div class="text-muted">Qty: ${item.quantity} × NPR <fmt:formatNumber value="${item.unitPrice}" type="number" maxFractionDigits="2"/></div>
                </div>
                <div class="cart-item-price">NPR <fmt:formatNumber value="${item.subtotal}" type="number" maxFractionDigits="2"/></div>
            </div>
        </c:forEach>
    </div>
    <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-secondary mt-2">Back to Orders</a>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


