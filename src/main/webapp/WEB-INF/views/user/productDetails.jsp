<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="${product.name}" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="container" style="padding:36px 20px;">
    <p class="text-muted mb-2">
        <a href="${pageContext.request.contextPath}/products">Shop</a> &rsaquo; ${product.categoryName} &rsaquo; ${product.name}
    </p>

    <div class="product-detail-grid">
        <div class="product-detail-img">
            <img src="${pageContext.request.contextPath}${product.imagePath}" alt="${product.name}">
        </div>
        <div class="product-detail-body">
            <div class="product-detail-brand">${product.brand}</div>
            <h1>${product.name}</h1>
            <div class="product-detail-price">
                NPR <fmt:formatNumber value="${product.price}" type="number" maxFractionDigits="0"/>
            </div>
            <p class="product-detail-desc">${product.description}</p>

            <div style="margin-bottom:16px;">
                <strong>SKU:</strong> <span class="text-muted">${product.sku}</span> &nbsp;|&nbsp;
                <strong>Category:</strong> <span class="text-muted">${product.categoryName}</span>
            </div>

            <c:choose>
                <c:when test="${product.stockQuantity > 0}">
                    <p style="color:#155724;margin-bottom:16px;">&#10003; In Stock (${product.stockQuantity} available)</p>
                </c:when>
                <c:otherwise>
                    <p style="color:#721c24;margin-bottom:16px;">&#10007; Out of Stock</p>
                </c:otherwise>
            </c:choose>

            <c:if test="${not empty sessionScope.loggedInUser and sessionScope.loggedInUser.role == 'CUSTOMER'}">
                <div class="product-detail-actions">
                    <c:if test="${product.stockQuantity > 0}">
                        <form action="${pageContext.request.contextPath}/user/order" method="post" style="display:flex;gap:8px;align-items:center;">
                            <input type="hidden" name="action" value="addToCart">
                            <input type="hidden" name="productId" value="${product.productId}">
                            <div style="display:flex;align-items:center;border:1px solid var(--border);border-radius:6px;overflow:hidden;">
                                <button type="button" class="qty-btn" data-dir="down" style="padding:8px 12px;background:var(--green-pale);border:none;cursor:pointer;font-size:1.1rem;">−</button>
                                <input type="number" name="qty" value="1" min="1" max="${product.stockQuantity}"
                                       style="width:50px;text-align:center;border:none;padding:8px 0;font-size:.95rem;">
                                <button type="button" class="qty-btn" data-dir="up" style="padding:8px 12px;background:var(--green-pale);border:none;cursor:pointer;font-size:1.1rem;">+</button>
                            </div>
                            <button type="submit" class="btn btn-primary">Add to Cart</button>
                        </form>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/user/wishlist" method="post">
                        <input type="hidden" name="productId" value="${product.productId}">
                        <button type="submit" class="btn btn-outline" title="Wishlist">
                            ${inWishlist ? '&#10084; In Wishlist' : '&#9825; Add to Wishlist'}
                        </button>
                    </form>
                </div>
            </c:if>

            <c:if test="${empty sessionScope.loggedInUser}">
                <a href="${pageContext.request.contextPath}/auth?action=loginForm" class="btn btn-primary">Login to Buy</a>
            </c:if>
        </div>
    </div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


