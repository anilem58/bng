<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="My Wishlist" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header"><div class="container"><h1>My Wishlist</h1></div></div>
<div class="container" style="padding:24px 20px;">
<div class="layout-two-col">
    <div class="sidebar">
        <div class="sidebar-title">My Account</div>
        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/user/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/user/orders">My Orders</a>
            <a href="${pageContext.request.contextPath}/user/wishlist" class="active">Wishlist</a>
            <a href="${pageContext.request.contextPath}/user/profile">Profile Settings</a>
            <a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a>
        </nav>
    </div>
    <div>
        <c:choose>
            <c:when test="${not empty wishlistItems}">
                <div class="wishlist-grid">
                    <c:forEach var="item" items="${wishlistItems}">
                        <div class="wishlist-item">
                            <a href="${pageContext.request.contextPath}/products?action=detail&id=${item.productId}">
                                <img src="${pageContext.request.contextPath}${item.product.imagePath}" alt="${item.product.name}">
                            </a>
                            <div class="wishlist-item-body">
                                <div class="product-card-brand">${item.product.brand}</div>
                                <div class="product-card-name">${item.product.name}</div>
                                <div class="product-card-price mt-1">
                                    NPR <fmt:formatNumber value="${item.product.price}" type="number" maxFractionDigits="0"/>
                                </div>
                                <div style="display:flex;gap:6px;margin-top:10px;">
                                    <c:if test="${item.product.stockQuantity > 0}">
                                        <form action="${pageContext.request.contextPath}/user/order" method="post" style="flex:1;">
                                            <input type="hidden" name="action" value="addToCart">
                                            <input type="hidden" name="productId" value="${item.productId}">
                                            <input type="hidden" name="qty" value="1">
                                            <button type="submit" class="btn btn-primary btn-sm" style="width:100%;">Add to Cart</button>
                                        </form>
                                    </c:if>
                                    <form action="${pageContext.request.contextPath}/user/wishlist" method="post">
                                        <input type="hidden" name="action" value="remove">
                                        <input type="hidden" name="productId" value="${item.productId}">
                                        <button type="submit" class="btn btn-danger btn-sm">&#10007;</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card text-center" style="padding:50px;">
                    <p style="font-size:3rem;">&#10084;</p>
                    <h3>Your wishlist is empty</h3>
                    <p class="text-muted mt-1">Save products you love by clicking the wishlist button.</p>
                    <div class="mt-2"><a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Browse Products</a></div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


