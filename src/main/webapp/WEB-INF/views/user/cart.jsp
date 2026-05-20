<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Shopping Cart" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header"><div class="container"><h1>Shopping Cart</h1></div></div>
<div class="container" style="padding:24px 20px;max-width:900px;">
    <c:if test="${not empty cartError}"><div class="alert alert-danger">${cartError}</div></c:if>

    <c:set var="cart" value="${sessionScope.cart}" />
    <c:choose>
        <c:when test="${not empty cart}">
            <div style="display:grid;grid-template-columns:1fr 320px;gap:28px;align-items:start;">
                <div class="card">
                    <div class="card-header"><h3>Cart Items (${cart.size()})</h3></div>
                    <c:set var="total" value="0" />
                    <c:forEach var="item" items="${cart}">
                        <c:set var="total" value="${total + item.unitPrice * item.quantity}" />
                        <div class="cart-item">
                            <img class="cart-item-img" src="${pageContext.request.contextPath}${item.imagePath}" alt="${item.productName}">
                            <div style="flex:1;">
                                <div class="cart-item-name">${item.productName}</div>
                                <div class="text-muted">NPR <fmt:formatNumber value="${item.unitPrice}" type="number" maxFractionDigits="2"/> each</div>
                                <div class="mt-1 text-muted">Qty: ${item.quantity}</div>
                            </div>
                            <div style="text-align:right;">
                                <div class="cart-item-price">NPR <fmt:formatNumber value="${item.unitPrice * item.quantity}" type="number" maxFractionDigits="2"/></div>
                                <form action="${pageContext.request.contextPath}/user/order" method="post" class="mt-1">
                                    <input type="hidden" name="action" value="removeCart">
                                    <input type="hidden" name="productId" value="${item.productId}">
                                    <button type="submit" class="btn btn-danger btn-sm">Remove</button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="cart-summary">
                    <h3 style="margin-bottom:14px;">Order Summary</h3>
                    <div style="display:flex;justify-content:space-between;margin-bottom:8px;">
                        <span>Subtotal</span>
                        <span>NPR <fmt:formatNumber value="${total}" type="number" maxFractionDigits="2"/></span>
                    </div>
                    <div style="display:flex;justify-content:space-between;margin-bottom:16px;">
                        <span>Shipping</span><span class="text-muted">Free</span>
                    </div>
                    <div class="cart-total" style="display:flex;justify-content:space-between;">
                        <span>Total</span>
                        <span>NPR <fmt:formatNumber value="${total}" type="number" maxFractionDigits="2"/></span>
                    </div>
                    <form action="${pageContext.request.contextPath}/user/order" method="post">
                        <input type="hidden" name="action" value="checkout">
                        <div class="form-group">
                            <label>Shipping Address *</label>
                            <textarea name="shippingAddress" class="form-control" rows="3" required
                                      placeholder="Enter your delivery address">${sessionScope.loggedInUser.address}</textarea>
                        </div>
                        <button type="submit" class="btn btn-primary btn-block">Place Order</button>
                    </form>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card text-center" style="padding:50px;">
                <p style="font-size:3rem;">&#128722;</p>
                <h3>Your cart is empty</h3>
                <p class="text-muted mt-1">Browse our products and add some items to your cart.</p>
                <div class="mt-2">
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Shop Now</a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


