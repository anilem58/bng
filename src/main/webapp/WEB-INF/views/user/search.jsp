<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Search Results" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header">
    <div class="container">
        <h1>Search Results</h1>
        <c:if test="${not empty keyword}">
            <p class="text-muted">Results for: "<strong>${keyword}</strong>"</p>
        </c:if>
    </div>
</div>
<div class="container" style="padding:20px;">
    <c:choose>
        <c:when test="${not empty products}">
            <p class="text-muted mb-2">${products.size()} product(s) found</p>
            <div class="product-grid">
                <c:forEach var="p" items="${products}">
                    <div class="product-card">
                        <img class="product-card-img"
                             src="${pageContext.request.contextPath}${p.imagePath}" alt="${p.name}">
                        <div class="product-card-body">
                            <div class="product-card-brand">${p.brand}</div>
                            <div class="product-card-name">${p.name}</div>
                            <div class="product-card-price">
                                NPR <fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/>
                            </div>
                            <div class="product-card-actions mt-1">
                                <a href="${pageContext.request.contextPath}/products?action=detail&id=${p.productId}"
                                   class="btn btn-primary btn-sm">View</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info">No products match your search. <a href="${pageContext.request.contextPath}/products">Browse all products</a></div>
        </c:otherwise>
    </c:choose>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


