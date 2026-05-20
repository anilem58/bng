<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="site-nav">
    <ul>
        <li><a href="${pageContext.request.contextPath}/">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/products">Shop</a></li>
        <li><a href="${pageContext.request.contextPath}/about.jsp">About</a></li>
        <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
        <c:if test="${sessionScope.loggedInUser.role == 'ADMIN'}">
            <li><a href="${pageContext.request.contextPath}/admin/dashboard">Admin Panel</a></li>
        </c:if>
        <c:if test="${sessionScope.loggedInUser.role == 'CUSTOMER'}">
            <li><a href="${pageContext.request.contextPath}/user/orders">My Orders</a></li>
        </c:if>
    </ul>
</nav>


