<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Contact Us" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main>
<div class="page-header">
    <div class="container">
        <h1>Contact Us</h1>
        <p class="text-muted">We'd love to hear from you</p>
    </div>
</div>

<div class="container" style="padding:40px 20px;max-width:900px;">
    <div style="display:grid;grid-template-columns:1fr 1fr;gap:40px;">

        <div>
            <c:if test="${param.msg eq 'sent'}">
                <div class="alert alert-success alert-auto">Thank you! Your message has been sent. We'll be in touch soon.</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <div class="card">
                <div class="card-header"><h3>Send Us a Message</h3></div>
                <form action="${pageContext.request.contextPath}/contact" method="post" novalidate>
                    <div class="form-group">
                        <label for="name">Your Name *</label>
                        <input type="text" id="name" name="name" class="form-control" required placeholder="Jane Doe">
                    </div>
                    <div class="form-group">
                        <label for="email">Email Address *</label>
                        <input type="email" id="email" name="email" class="form-control" required placeholder="jane@example.com">
                    </div>
                    <div class="form-group">
                        <label for="subject">Subject *</label>
                        <input type="text" id="subject" name="subject" class="form-control" required placeholder="How can we help?">
                    </div>
                    <div class="form-group">
                        <label for="message">Message *</label>
                        <textarea id="message" name="message" class="form-control" rows="5" required placeholder="Write your message here..."></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">Send Message</button>
                </form>
            </div>
        </div>

        <div>
            <div class="card">
                <div class="card-header"><h3>Get in Touch</h3></div>
                <p class="text-muted mb-2">We typically respond within 24 hours on business days.</p>
                <div style="display:flex;flex-direction:column;gap:16px;">
                    <div style="display:flex;gap:12px;align-items:flex-start;">
                        <span style="font-size:1.5rem;">📍</span>
                        <div><strong>Address</strong><br><span class="text-muted">Kathmandu, Nepal</span></div>
                    </div>
                    <div style="display:flex;gap:12px;align-items:flex-start;">
                        <span style="font-size:1.5rem;">✉️</span>
                        <div><strong>Email</strong><br><span class="text-muted">info@blissandglow.com</span></div>
                    </div>
                    <div style="display:flex;gap:12px;align-items:flex-start;">
                        <span style="font-size:1.5rem;">📞</span>
                        <div><strong>Phone</strong><br><span class="text-muted">+977 98XXXXXXXX</span></div>
                    </div>
                    <div style="display:flex;gap:12px;align-items:flex-start;">
                        <span style="font-size:1.5rem;">🕐</span>
                        <div><strong>Business Hours</strong><br><span class="text-muted">Sun–Fri: 9am – 6pm NPT</span></div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
</main>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>


