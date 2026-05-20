<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Manage Categories" />
<%@ include file="../common/header.jsp" %>
<main>
<div class="page-header"><div class="container"><h1>Manage Categories</h1></div></div>
<div class="container" style="padding:24px 20px;max-width:900px;">
    <c:set var="flashError" value="${sessionScope.flashError}" />
    <c:remove var="flashError" scope="session"/>
    <c:if test="${not empty flashError}"><div class="alert alert-danger">${flashError}</div></c:if>

    <div style="display:grid;grid-template-columns:1fr 1fr;gap:28px;align-items:start;">
        <!-- Add category form -->
        <div class="card">
            <div class="card-header"><h3>Add Category</h3></div>
            <form action="${pageContext.request.contextPath}/admin/categories/add" method="post" novalidate>
                <div class="form-group">
                    <label>Name *</label>
                    <input type="text" name="name" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>Description</label>
                    <textarea name="description" class="form-control" rows="3"></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Add Category</button>
            </form>
        </div>

        <!-- List -->
        <div class="card">
            <div class="card-header"><h3>All Categories</h3></div>
            <c:forEach var="cat" items="${categories}">
                <div style="display:flex;justify-content:space-between;align-items:center;padding:10px 0;border-bottom:1px solid var(--border);">
                    <div>
                        <strong>${cat.name}</strong>
                        <div class="text-muted" style="font-size:.82rem;">${cat.description} &mdash; ${cat.productCount} products</div>
                    </div>
                    <div style="display:flex;gap:6px;">
                        <button onclick="editCategory(${cat.categoryId},'${fn:escapeXml(cat.name)}','${fn:escapeXml(cat.description)}')"
                                class="btn btn-secondary btn-sm">Edit</button>
                        <form action="${pageContext.request.contextPath}/admin/categories/delete" method="post">
                            <input type="hidden" name="categoryId" value="${cat.categoryId}">
                            <button type="submit" class="btn btn-danger btn-sm btn-confirm-delete">Del</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Edit modal (simple inline) -->
    <div id="editModal" style="display:none;position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,.5);z-index:200;align-items:center;justify-content:center;">
        <div style="background:var(--white);border-radius:var(--radius);padding:28px;width:380px;max-width:90vw;">
            <h3 style="margin-bottom:18px;">Edit Category</h3>
            <form action="${pageContext.request.contextPath}/admin/categories/edit" method="post" novalidate>
                <input type="hidden" id="editCategoryId" name="categoryId">
                <div class="form-group">
                    <label>Name *</label>
                    <input type="text" id="editCategoryName" name="name" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>Description</label>
                    <textarea id="editCategoryDesc" name="description" class="form-control" rows="3"></textarea>
                </div>
                <div style="display:flex;gap:10px;">
                    <button type="submit" class="btn btn-primary">Save</button>
                    <button type="button" onclick="document.getElementById('editModal').style.display='none'" class="btn btn-secondary">Cancel</button>
                </div>
            </form>
        </div>
    </div>
</div>
</main>
<script>
function editCategory(id, name, desc) {
    document.getElementById('editCategoryId').value = id;
    document.getElementById('editCategoryName').value = name;
    document.getElementById('editCategoryDesc').value = desc;
    document.getElementById('editModal').style.display = 'flex';
}
</script>
<%@ include file="../common/footer.jsp" %>


