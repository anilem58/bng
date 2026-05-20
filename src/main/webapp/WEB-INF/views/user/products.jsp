<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Shop" />
<%@ include file="../common/header.jsp" %>

<style>
.product-section { margin-bottom: 50px; }
.section-heading {
    display: flex;
    align-items: center;
    gap: 14px;
    margin-bottom: 20px;
    padding-bottom: 10px;
    border-bottom: 2px solid #4a7c59;
}
.section-heading h2 {
    margin: 0;
    font-size: 1.6rem;
    color: #2d4a35;
}
.section-heading .section-tag {
    background: #4a7c59;
    color: #fff;
    font-size: 0.75rem;
    padding: 3px 10px;
    border-radius: 20px;
}
.section-nav {
    display: flex;
    gap: 12px;
    margin-bottom: 36px;
    flex-wrap: wrap;
}
.section-nav a {
    padding: 8px 22px;
    border-radius: 25px;
    border: 2px solid #4a7c59;
    color: #4a7c59;
    font-weight: 600;
    text-decoration: none;
    transition: all 0.2s;
}
.section-nav a:hover {
    background: #4a7c59;
    color: #fff;
}
</style>

<main>
<div class="page-header">
    <div class="container">
        <h1>Shop All Products</h1>
        <p style="color:#ccc;margin-top:6px;">Browse by category below</p>
    </div>
</div>

<div class="container" style="padding:30px 20px;">

    <!-- Quick-jump links -->
    <div class="section-nav">
        <a href="#skincare">Skincare</a>
        <a href="#makeup">Makeup</a>
        <a href="#haircare">Haircare</a>
    </div>

    <!-- ── Skincare ─────────────────────────────────────── -->
    <section class="product-section" id="skincare">
        <div class="section-heading">
            <h2>Skincare</h2>
            <span class="section-tag">${fn:length(skincareProducts)} products</span>
        </div>
        <c:choose>
            <c:when test="${not empty skincareProducts}">
                <div class="product-grid">
                    <c:forEach var="p" items="${skincareProducts}">
                        <div class="product-card">
                            <img class="product-card-img"
                                 src="${pageContext.request.contextPath}${p.imagePath}"
                                 alt="${p.name}">
                            <div class="product-card-body">
                                <div class="product-card-brand">${p.brand}</div>
                                <div class="product-card-name">${p.name}</div>
                                <div class="product-card-price">
                                    NPR <fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/>
                                </div>
                                <c:if test="${p.stockQuantity == 0}">
                                    <span class="badge-out">Out of Stock</span>
                                </c:if>
                                <div class="product-card-actions mt-1">
                                    <a href="${pageContext.request.contextPath}/products?action=detail&id=${p.productId}"
                                       class="btn btn-secondary btn-sm">View</a>
                                    <c:if test="${p.stockQuantity > 0 and not empty sessionScope.loggedInUser and sessionScope.loggedInUser.role == 'CUSTOMER'}">
                                        <form action="${pageContext.request.contextPath}/user/order" method="post" style="flex:1;">
                                            <input type="hidden" name="action" value="addToCart">
                                            <input type="hidden" name="productId" value="${p.productId}">
                                            <input type="hidden" name="qty" value="1">
                                            <button type="submit" class="btn btn-primary btn-sm" style="width:100%;">Cart</button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">No skincare products found.</div>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- ── Makeup ───────────────────────────────────────── -->
    <section class="product-section" id="makeup">
        <div class="section-heading">
            <h2>Makeup</h2>
            <span class="section-tag">${fn:length(makeupProducts)} products</span>
        </div>
        <c:choose>
            <c:when test="${not empty makeupProducts}">
                <div class="product-grid">
                    <c:forEach var="p" items="${makeupProducts}">
                        <div class="product-card">
                            <img class="product-card-img"
                                 src="${pageContext.request.contextPath}${p.imagePath}"
                                 alt="${p.name}">
                            <div class="product-card-body">
                                <div class="product-card-brand">${p.brand}</div>
                                <div class="product-card-name">${p.name}</div>
                                <div class="product-card-price">
                                    NPR <fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/>
                                </div>
                                <c:if test="${p.stockQuantity == 0}">
                                    <span class="badge-out">Out of Stock</span>
                                </c:if>
                                <div class="product-card-actions mt-1">
                                    <a href="${pageContext.request.contextPath}/products?action=detail&id=${p.productId}"
                                       class="btn btn-secondary btn-sm">View</a>
                                    <c:if test="${p.stockQuantity > 0 and not empty sessionScope.loggedInUser and sessionScope.loggedInUser.role == 'CUSTOMER'}">
                                        <form action="${pageContext.request.contextPath}/user/order" method="post" style="flex:1;">
                                            <input type="hidden" name="action" value="addToCart">
                                            <input type="hidden" name="productId" value="${p.productId}">
                                            <input type="hidden" name="qty" value="1">
                                            <button type="submit" class="btn btn-primary btn-sm" style="width:100%;">Cart</button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">No makeup products found.</div>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- ── Haircare ──────────────────────────────────────── -->
    <section class="product-section" id="haircare">
        <div class="section-heading">
            <h2>Haircare</h2>
            <span class="section-tag">${fn:length(haircareProducts)} products</span>
        </div>
        <c:choose>
            <c:when test="${not empty haircareProducts}">
                <div class="product-grid">
                    <c:forEach var="p" items="${haircareProducts}">
                        <div class="product-card">
                            <img class="product-card-img"
                                 src="${pageContext.request.contextPath}${p.imagePath}"
                                 alt="${p.name}">
                            <div class="product-card-body">
                                <div class="product-card-brand">${p.brand}</div>
                                <div class="product-card-name">${p.name}</div>
                                <div class="product-card-price">
                                    NPR <fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/>
                                </div>
                                <c:if test="${p.stockQuantity == 0}">
                                    <span class="badge-out">Out of Stock</span>
                                </c:if>
                                <div class="product-card-actions mt-1">
                                    <a href="${pageContext.request.contextPath}/products?action=detail&id=${p.productId}"
                                       class="btn btn-secondary btn-sm">View</a>
                                    <c:if test="${p.stockQuantity > 0 and not empty sessionScope.loggedInUser and sessionScope.loggedInUser.role == 'CUSTOMER'}">
                                        <form action="${pageContext.request.contextPath}/user/order" method="post" style="flex:1;">
                                            <input type="hidden" name="action" value="addToCart">
                                            <input type="hidden" name="productId" value="${p.productId}">
                                            <input type="hidden" name="qty" value="1">
                                            <button type="submit" class="btn btn-primary btn-sm" style="width:100%;">Cart</button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">No haircare products found.</div>
            </c:otherwise>
        </c:choose>
    </section>

</div>
</main>
<%@ include file="../common/footer.jsp" %>
