<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Manage Products" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header">
    <div class="container" style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:12px;">
        <div><h1>Manage Products</h1></div>
        <a href="${pageContext.request.contextPath}/admin/products/add" class="btn btn-primary">+ Add Product</a>
    </div>
</div>
<div class="container" style="padding:24px 20px;">
    <c:if test="${param.msg eq 'added'}"><div class="alert alert-success alert-auto">Product added successfully.</div></c:if>
    <c:if test="${param.msg eq 'updated'}"><div class="alert alert-success alert-auto">Product updated successfully.</div></c:if>
    <c:if test="${param.msg eq 'deleted'}"><div class="alert alert-success alert-auto">Product deleted.</div></c:if>

    <div class="card">
        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>IMG</th><th>Name</th><th>Brand</th><th>Category</th><th>Price</th><th>Stock</th><th>SKU</th><th>Actions</th></tr></thead>
                <tbody>
                    <c:forEach var="p" items="${products}">
                        <tr>
                            <td><img src="${pageContext.request.contextPath}${p.imagePath}" alt="${p.name}" style="width:48px;height:48px;object-fit:cover;border-radius:6px;"></td>
                            <td>${p.name}</td>
                            <td>${p.brand}</td>
                            <td>${p.categoryName}</td>
                            <td>NPR <fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.stockQuantity == 0}"><span class="badge-out">0</span></c:when>
                                    <c:otherwise>${p.stockQuantity}</c:otherwise>
                                </c:choose>
                            </td>
                            <td><small>${p.sku}</small></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/products/edit?id=${p.productId}" class="btn btn-secondary btn-sm">Edit</a>
                                <form action="${pageContext.request.contextPath}/admin/products/delete" method="post" style="display:inline;">
                                    <input type="hidden" name="productId" value="${p.productId}">
                                    <button type="submit" class="btn btn-danger btn-sm btn-confirm-delete">Delete</button>
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


