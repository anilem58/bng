<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Add Product" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header"><div class="container"><h1>Add New Product</h1></div></div>
<div class="container" style="padding:24px 20px;max-width:700px;">
    <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
    <div class="card">
        <form action="${pageContext.request.contextPath}/admin/products/add" method="post" enctype="multipart/form-data" novalidate>
            <div class="form-group">
                <label>Product Name *</label>
                <input type="text" name="name" class="form-control" required>
            </div>
            <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px;">
                <div class="form-group">
                    <label>Brand *</label>
                    <input type="text" name="brand" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>SKU *</label>
                    <input type="text" name="sku" class="form-control" required placeholder="BNG-CLN-001">
                </div>
            </div>
            <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px;">
                <div class="form-group">
                    <label>Price (NPR) *</label>
                    <input type="number" name="price" class="form-control" required min="0" step="0.01">
                </div>
                <div class="form-group">
                    <label>Stock Quantity *</label>
                    <input type="number" name="stockQuantity" class="form-control" required min="0">
                </div>
            </div>
            <div class="form-group">
                <label>Category *</label>
                <select name="categoryId" class="form-control" required>
                    <option value="">— Select Category —</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.categoryId}">${cat.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Description</label>
                <textarea name="description" class="form-control" rows="4"></textarea>
            </div>
            <div class="form-group">
                <label>Product Image</label>
                <input type="file" id="imageInput" name="image" class="form-control" accept="image/*">
                <img id="imagePreview" src="" alt="Preview" style="display:none;margin-top:10px;max-height:160px;border-radius:6px;">
            </div>
            <div style="display:flex;gap:12px;">
                <button type="submit" class="btn btn-primary">Add Product</button>
                <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
</main>
<%@ include file="../common/footer.jsp" %>


