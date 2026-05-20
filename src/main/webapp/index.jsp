<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Home" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main>

<!-- Hero -->
<section class="hero">
    <div class="container">
        <h1>Beauty. Production. You.</h1>
        <p>Discover skincare and cosmetics that celebrate your natural glow — curated with love, delivered with care.</p>
        <div class="hero-buttons">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-white">Shop Now</a>
            <a href="${pageContext.request.contextPath}/about.jsp" class="btn btn-outline" style="border-color:#fff;color:#fff;">Our Story</a>
        </div>
    </div>
</section>

<!-- Product Sections -->
<div class="container" style="padding:40px 20px;">

    <%-- Reusable product card macro via inclusion --%>

    <!-- Skincare -->
    <section style="margin-bottom:50px;">
        <div style="display:flex;align-items:center;justify-content:space-between;border-bottom:2px solid #4a7c59;padding-bottom:10px;margin-bottom:20px;">
            <h2 style="margin:0;color:#2d4a35;font-size:1.5rem;">Skincare</h2>
            <a href="${pageContext.request.contextPath}/products#skincare" class="btn btn-secondary btn-sm">View All</a>
        </div>
        <c:choose>
            <c:when test="${not empty skincareProducts}">
                <div class="product-grid">
                    <c:forEach var="p" items="${skincareProducts}">
                        <div class="product-card">
                            <img class="product-card-img" src="${pageContext.request.contextPath}${p.imagePath}" alt="${p.name}">
                            <div class="product-card-body">
                                <div class="product-card-brand">${p.brand}</div>
                                <div class="product-card-name">${p.name}</div>
                                <div class="product-card-price">NPR <fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/></div>
                                <c:if test="${p.stockQuantity == 0}"><span class="badge-out">Out of Stock</span></c:if>
                                <div class="product-card-actions mt-1">
                                    <a href="${pageContext.request.contextPath}/products?action=detail&id=${p.productId}" class="btn btn-secondary btn-sm">View</a>
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
            <c:otherwise><p class="text-muted">No skincare products available.</p></c:otherwise>
        </c:choose>
    </section>

    <!-- Makeup -->
    <section style="margin-bottom:50px;">
        <div style="display:flex;align-items:center;justify-content:space-between;border-bottom:2px solid #4a7c59;padding-bottom:10px;margin-bottom:20px;">
            <h2 style="margin:0;color:#2d4a35;font-size:1.5rem;">Makeup</h2>
            <a href="${pageContext.request.contextPath}/products#makeup" class="btn btn-secondary btn-sm">View All</a>
        </div>
        <c:choose>
            <c:when test="${not empty makeupProducts}">
                <div class="product-grid">
                    <c:forEach var="p" items="${makeupProducts}">
                        <div class="product-card">
                            <img class="product-card-img" src="${pageContext.request.contextPath}${p.imagePath}" alt="${p.name}">
                            <div class="product-card-body">
                                <div class="product-card-brand">${p.brand}</div>
                                <div class="product-card-name">${p.name}</div>
                                <div class="product-card-price">NPR <fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/></div>
                                <c:if test="${p.stockQuantity == 0}"><span class="badge-out">Out of Stock</span></c:if>
                                <div class="product-card-actions mt-1">
                                    <a href="${pageContext.request.contextPath}/products?action=detail&id=${p.productId}" class="btn btn-secondary btn-sm">View</a>
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
            <c:otherwise><p class="text-muted">No makeup products available.</p></c:otherwise>
        </c:choose>
    </section>

    <!-- Haircare -->
    <section style="margin-bottom:50px;">
        <div style="display:flex;align-items:center;justify-content:space-between;border-bottom:2px solid #4a7c59;padding-bottom:10px;margin-bottom:20px;">
            <h2 style="margin:0;color:#2d4a35;font-size:1.5rem;">Haircare</h2>
            <a href="${pageContext.request.contextPath}/products#haircare" class="btn btn-secondary btn-sm">View All</a>
        </div>
        <c:choose>
            <c:when test="${not empty haircareProducts}">
                <div class="product-grid">
                    <c:forEach var="p" items="${haircareProducts}">
                        <div class="product-card">
                            <img class="product-card-img" src="${pageContext.request.contextPath}${p.imagePath}" alt="${p.name}">
                            <div class="product-card-body">
                                <div class="product-card-brand">${p.brand}</div>
                                <div class="product-card-name">${p.name}</div>
                                <div class="product-card-price">NPR <fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/></div>
                                <c:if test="${p.stockQuantity == 0}"><span class="badge-out">Out of Stock</span></c:if>
                                <div class="product-card-actions mt-1">
                                    <a href="${pageContext.request.contextPath}/products?action=detail&id=${p.productId}" class="btn btn-secondary btn-sm">View</a>
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
            <c:otherwise><p class="text-muted">No haircare products available.</p></c:otherwise>
        </c:choose>
    </section>

</div>

<!-- Brand highlights -->
<section style="background:var(--green-pale);padding:50px 20px;">
    <div class="container">
        <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:28px;text-align:center;">
            <div>
                <div style="font-size:2.5rem;">🌿</div>
                <h3 style="margin:10px 0 6px;">Natural Ingredients</h3>
                <p class="text-muted">Formulas inspired by nature, gentle on every skin type.</p>
            </div>
            <div>
                <div style="font-size:2.5rem;">🐰</div>
                <h3 style="margin:10px 0 6px;">Cruelty Free</h3>
                <p class="text-muted">We never test on animals — ever.</p>
            </div>
            <div>
                <div style="font-size:2.5rem;">♻️</div>
                <h3 style="margin:10px 0 6px;">Sustainable</h3>
                <p class="text-muted">Eco-conscious packaging and responsible sourcing.</p>
            </div>
            <div>
                <div style="font-size:2.5rem;">💚</div>
                <h3 style="margin:10px 0 6px;">Made with Love</h3>
                <p class="text-muted">Each product crafted to make you feel beautiful.</p>
            </div>
        </div>
    </div>
</section>

</main>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>


