<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="About Us" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main>
<div class="page-header">
    <div class="container">
        <h1>Our Story</h1>
        <p class="text-muted">Beauty. Production. You.</p>
    </div>
</div>

<div class="container" style="padding:40px 20px;max-width:860px;">

    <div style="display:grid;grid-template-columns:1fr 1fr;gap:40px;align-items:center;margin-bottom:50px;">
        <div>
            <h2 style="color:var(--green-dark);margin-bottom:16px;">Born from a passion for clean beauty</h2>
            <p style="color:var(--text-mid);line-height:1.8;margin-bottom:14px;">
                Bliss and Glow was founded in 2019 by a group of beauty enthusiasts who believed that skincare should
                be effective, honest, and accessible to everyone. Frustrated by products full of harmful chemicals
                and misleading claims, we set out to build something different.
            </p>
            <p style="color:var(--text-mid);line-height:1.8;">
                From a small kitchen lab in Kathmandu, Nepal, we developed our first line of plant-based serums and
                moisturizers. Today, Bliss and Glow is a trusted beauty brand offering over 20 carefully formulated
                products that celebrate natural beauty.
            </p>
        </div>
        <div style="text-align:center;">
            <img src="${pageContext.request.contextPath}/assets/images/logo/bng-logo.png" alt="Bliss and Glow Logo"
                 style="max-width:220px;margin:0 auto;">
        </div>
    </div>

    <hr style="border:none;border-top:2px solid var(--border);margin:40px 0;">

    <h2 class="section-title">Our Values</h2>
    <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(240px,1fr));gap:22px;margin-bottom:40px;">
        <div class="card">
            <div style="font-size:2rem;margin-bottom:10px;">🌱</div>
            <h3>Clean Formulas</h3>
            <p class="text-muted mt-1">No parabens, sulphates, or synthetic fragrance. We publish every ingredient.</p>
        </div>
        <div class="card">
            <div style="font-size:2rem;margin-bottom:10px;">🐰</div>
            <h3>Cruelty-Free</h3>
            <p class="text-muted mt-1">Certified cruelty-free. Never tested on animals at any stage.</p>
        </div>
        <div class="card">
            <div style="font-size:2rem;margin-bottom:10px;">♻️</div>
            <h3>Eco Packaging</h3>
            <p class="text-muted mt-1">Recyclable, minimal packaging designed to reduce waste.</p>
        </div>
        <div class="card">
            <div style="font-size:2rem;margin-bottom:10px;">🤝</div>
            <h3>Transparent</h3>
            <p class="text-muted mt-1">We believe you deserve to know exactly what you're putting on your skin.</p>
        </div>
    </div>

    <h2 class="section-title">Meet the Team</h2>
    <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(180px,1fr));gap:20px;margin-bottom:40px;">
        <div style="text-align:center;">
            <div style="width:80px;height:80px;border-radius:50%;background:var(--green-pale);margin:0 auto 10px;display:flex;align-items:center;justify-content:center;font-size:2rem;">👩</div>
            <strong>Priya Sharma</strong>
            <p class="text-muted" style="font-size:.85rem;">Founder &amp; CEO</p>
        </div>
        <div style="text-align:center;">
            <div style="width:80px;height:80px;border-radius:50%;background:var(--green-pale);margin:0 auto 10px;display:flex;align-items:center;justify-content:center;font-size:2rem;">🧑‍🔬</div>
            <strong>Raj Kumar</strong>
            <p class="text-muted" style="font-size:.85rem;">Head of R&amp;D</p>
        </div>
        <div style="text-align:center;">
            <div style="width:80px;height:80px;border-radius:50%;background:var(--green-pale);margin:0 auto 10px;display:flex;align-items:center;justify-content:center;font-size:2rem;">👩‍💻</div>
            <strong>Aisha Pandey</strong>
            <p class="text-muted" style="font-size:.85rem;">Marketing Director</p>
        </div>
    </div>

    <div class="text-center" style="margin-bottom:40px;">
        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary" style="margin-right:10px;">Shop Our Products</a>
        <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline">Get in Touch</a>
    </div>
</div>
</main>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>


