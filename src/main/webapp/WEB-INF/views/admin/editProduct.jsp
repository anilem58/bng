<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Edit Product" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header"><div class="container"><h1>Edit Product</h1></div></div>
<div class="container" style="padding:24px 20px;max-width:700px;">
    <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
    <div class="card">
        <form action="${pageContext.request.contextPath}/admin/products/edit" method="post" enctype="multipart/form-data" novalidate>
            <input type="hidden" name="productId" value="${product.productId}">
            <input type="hidden" name="existingImage" value="${product.imagePath}">

            <div class="form-group">
                <label>Product Name *</label>
                <input type="text" name="name" class="form-control" required value="${fn:escapeXml(product.name)}">
            </div>
            <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px;">
                <div class="form-group">
                    <label>Brand *</label>
                    <input type="text" name="brand" class="form-control" required value="${fn:escapeXml(product.brand)}">
                </div>
                <div class="form-group">
                    <label>SKU *</label>
                    <input type="text" name="sku" class="form-control" required value="${fn:escapeXml(product.sku)}">
                </div>
            </div>
            <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px;">
                <div class="form-group">
                    <label>Price (NPR) *</label>
                    <input type="number" name="price" class="form-control" required min="0" step="0.01" value="${product.price}">
                </div>
                <div class="form-group">
                    <label>Stock Quantity *</label>
                    <input type="number" name="stockQuantity" class="form-control" required min="0" value="${product.stockQuantity}">
                </div>
            </div>
            <div class="form-group">
                <label>Category *</label>
                <select name="categoryId" class="form-control" required>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.categoryId}" ${cat.categoryId == product.categoryId ? 'selected' : ''}>${cat.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Description</label>
                <textarea name="description" class="form-control" rows="4">${fn:escapeXml(product.description)}</textarea>
            </div>
            <div class="form-group">
                <label>Current Image</label>
                <img id="imagePreview" src="${pageContext.request.contextPath}${product.imagePath}" alt="Current"
                     style="display:block;max-height:160px;border-radius:6px;margin-bottom:10px;">
                <label>Replace Image</label>
                <input type="file" id="imageInput" name="image" class="form-control" accept="image/*">
            </div>
            <div style="display:flex;gap:12px;">
                <button type="submit" class="btn btn-primary">Save Changes</button>
                <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


