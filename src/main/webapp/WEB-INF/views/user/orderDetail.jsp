<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Order #${order.orderId}" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header"><div class="container"><h1>Order #${order.orderId}</h1></div></div>
<div class="container" style="padding:24px 20px;max-width:800px;">
    <c:if test="${param.msg eq 'placed'}"><div class="alert alert-success alert-auto">&#10003; Your order has been placed successfully!</div></c:if>

    <div class="card mb-2">
        <div class="card-header"><h3>Order Details</h3></div>
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;">
            <div><strong>Status:</strong><br><span class="status status-${order.status.toLowerCase()}">${order.status}</span></div>
            <div><strong>Order Date:</strong><br>${order.orderDate}</div>
            <div><strong>Shipping Address:</strong><br>${order.shippingAddress}</div>
            <div><strong>Total Amount:</strong><br>
                <span style="font-size:1.2rem;font-weight:700;color:var(--green);">
                    NPR <fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="2"/>
                </span>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-header"><h3>Items Ordered</h3></div>
        <c:forEach var="item" items="${order.items}">
            <div class="cart-item">
                <img class="cart-item-img" src="${pageContext.request.contextPath}${item.imagePath}" alt="${item.productName}">
                <div style="flex:1;">
                    <div class="cart-item-name">${item.productName}</div>
                    <div class="text-muted">Qty: ${item.quantity} × NPR <fmt:formatNumber value="${item.unitPrice}" type="number" maxFractionDigits="2"/></div>
                </div>
                <div class="cart-item-price">
                    NPR <fmt:formatNumber value="${item.subtotal}" type="number" maxFractionDigits="2"/>
                </div>
            </div>
        </c:forEach>
    </div>

    <div class="mt-2 d-flex gap-1 flex-wrap">
        <a href="${pageContext.request.contextPath}/user/orders" class="btn btn-secondary">Back to Orders</a>
        <c:if test="${order.status eq 'PENDING'}">
            <form action="${pageContext.request.contextPath}/user/order" method="post">
                <input type="hidden" name="action" value="cancel">
                <input type="hidden" name="orderId" value="${order.orderId}">
                <button type="submit" class="btn btn-danger btn-confirm-delete">Cancel Order</button>
            </form>
        </c:if>
    </div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


