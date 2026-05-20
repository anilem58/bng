<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty pageTitle ? 'Bliss and Glow' : pageTitle.concat(' — Bliss and Glow')}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<header class="site-header">
    <div class="header-inner">
        <a href="${pageContext.request.contextPath}/" class="logo">
            <img src="${pageContext.request.contextPath}/assets/images/logo/bng-logo.png" alt="Bliss and Glow Logo">
            <div>
                <span class="logo-text">Bliss &amp; Glow</span>
                <span class="tagline">Beauty. Production. You.</span>
            </div>
        </a>

        <form class="search-bar-header" action="${pageContext.request.contextPath}/products" method="get">
            <input type="hidden" name="action" value="search">
            <input type="text" name="q" placeholder="Search products or brands…" value="${fn:escapeXml(param.q)}" autocomplete="off">
            <button type="submit" title="Search">&#128269;</button>
        </form>

        <nav class="header-nav">
            <c:choose>
                <c:when test="${not empty sessionScope.loggedInUser}">
                    <c:if test="${sessionScope.loggedInUser.role == 'CUSTOMER'}">
                        <a href="${pageContext.request.contextPath}/user/wishlist">&#10084; Wishlist</a>
                        <a href="${pageContext.request.contextPath}/user/order?action=cart">&#128722; Cart</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/${sessionScope.loggedInUser.role == 'ADMIN' ? 'admin' : 'user'}/dashboard">
                        Hi, ${sessionScope.loggedInUser.fullName.split(' ')[0]}
                    </a>
                    <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn btn-secondary btn-sm">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/auth?action=loginForm" class="btn btn-outline btn-sm">Login</a>
                    <a href="${pageContext.request.contextPath}/auth?action=registerForm" class="btn btn-primary btn-sm">Register</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>
<%@ include file="navbar.jsp" %>


