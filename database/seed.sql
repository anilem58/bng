-- =====================================================================
-- Bliss and Glow Seed Data
-- Run this AFTER schema.sql
-- Default admin login:  admin@blissandglow.com  /  Admin@123
-- (BCrypt hash below was generated for "Admin@123")
-- =====================================================================
USE blissandglow_db;

-- ---------------------------------------------------------------------
-- Default admin (password: Admin@123 — BCrypt hashed)
-- ---------------------------------------------------------------------
INSERT INTO users (full_name, email, phone, password_hash, dob, address, role, status) VALUES
('System Administrator', 'admin@blissandglow.com', '9800000000',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 '1990-01-01', 'Pokhara, Nepal', 'ADMIN', 'APPROVED');

-- A sample approved customer for testing (password: Customer@123)
INSERT INTO users (full_name, email, phone, password_hash, dob, address, role, status) VALUES
('Demo Customer', 'demo@blissandglow.com', '9811111111',
 '$2a$10$bV6SgDQpQ8lJbGY4sTjGm.5n5Vh8OmzS5nJ3Ix6VJhO9xNT5N0L9C',
 '1998-05-15', 'Kathmandu, Nepal', 'CUSTOMER', 'APPROVED');

-- ---------------------------------------------------------------------
-- Categories
-- ---------------------------------------------------------------------
INSERT INTO categories (name, description) VALUES
('Cleansers',    'Face cleansers, cleansing oils and makeup removers'),
('Moisturizers', 'Daily moisturizers, creams and hydrating gels'),
('Serums',       'Targeted serums and treatment essences'),
('Sunscreen',    'SPF protection for daily use'),
('Lip Care',     'Lipsticks, lip balms and lip treatments'),
('Makeup',       'Foundation, blush, mascara and other makeup'),
('Body Care',    'Body lotions, scrubs and body washes');

-- ---------------------------------------------------------------------
-- Products — 20 items mapped to /assets/images/products/1.jpeg .. 20.jpeg
-- (You can rename / re-describe these freely; the image paths stay the same.)
-- ---------------------------------------------------------------------
INSERT INTO products
    (name, brand, description, price, stock_quantity, category_id, image_path, sku) VALUES
('Madagascar Centella Light Cleansing Oil', 'SKIN1004',
 'A gentle cleansing oil made with pure Centella from Madagascar. Removes makeup and impurities while soothing the skin.',
 1850.00, 30, 1, '/assets/images/products/1.jpeg', 'BNG-CLN-001'),

('Hydrating Daily Moisturizer', 'Bliss & Glow',
 'A lightweight daily moisturizer that locks in hydration for up to 24 hours. Suitable for all skin types.',
 1450.00, 50, 2, '/assets/images/products/2.jpeg', 'BNG-MST-002'),

('Vitamin C Brightening Serum', 'Bliss & Glow',
 '15% vitamin C serum that brightens dull skin and fades dark spots over time.',
 2250.00, 25, 3, '/assets/images/products/3.jpeg', 'BNG-SER-003'),

('Niacinamide 10% Pore Refining Serum', 'Bliss & Glow',
 'Minimizes the appearance of pores and balances oily skin.',
 1750.00, 35, 3, '/assets/images/products/4.jpeg', 'BNG-SER-004'),

('Soft Pinch Liquid Lipstick', 'Rare Beauty',
 'A weightless, hydrating liquid lipstick with a soft matte finish.',
 1950.00, 40, 5, '/assets/images/products/5.jpeg', 'BNG-LIP-005'),

('Mineral SPF 50 Sunscreen', 'Bliss & Glow',
 'Broad-spectrum mineral sunscreen with a non-greasy finish. Reef-safe.',
 1650.00, 45, 4, '/assets/images/products/6.jpeg', 'BNG-SUN-006'),

('Glow Drops Illuminating Serum', 'Bliss & Glow',
 'Pearlescent drops that give skin a healthy, lit-from-within glow.',
 2150.00, 20, 3, '/assets/images/products/7.jpeg', 'BNG-SER-007'),

('Rosehip Replenishing Night Cream', 'Bliss & Glow',
 'Rich night cream infused with rosehip oil to repair and hydrate while you sleep.',
 1950.00, 28, 2, '/assets/images/products/8.jpeg', 'BNG-MST-008'),

('Gentle Foaming Face Wash', 'Bliss & Glow',
 'Sulphate-free foaming cleanser that respects your skin barrier.',
 1150.00, 60, 1, '/assets/images/products/9.jpeg', 'BNG-CLN-009'),

('Hyaluronic Acid Hydra Serum', 'Bliss & Glow',
 'Multi-weight hyaluronic acid serum for intense, layered hydration.',
 1850.00, 32, 3, '/assets/images/products/10.jpeg', 'BNG-SER-010'),

('Tinted Lip Balm — Petal', 'Bliss & Glow',
 'A nourishing tinted balm with a soft pink wash of color.',
 950.00, 55, 5, '/assets/images/products/11.jpeg', 'BNG-LIP-011'),

('Velvet Matte Lipstick — Mauve', 'Bliss & Glow',
 'A long-wearing matte lipstick that does not dry out the lips.',
 1450.00, 38, 5, '/assets/images/products/12.jpeg', 'BNG-LIP-012'),

('Soft Focus Liquid Foundation', 'Bliss & Glow',
 'Buildable medium coverage foundation in 12 shades, with a natural satin finish.',
 2450.00, 22, 6, '/assets/images/products/13.jpeg', 'BNG-MUP-013'),

('Cream Blush — Sunset', 'Bliss & Glow',
 'Dewy cream blush that blends seamlessly for a flushed look.',
 1350.00, 30, 6, '/assets/images/products/14.jpeg', 'BNG-MUP-014'),

('Lengthening Mascara', 'Bliss & Glow',
 'Smudge-proof mascara that lifts and lengthens lashes without clumping.',
 1250.00, 50, 6, '/assets/images/products/15.jpeg', 'BNG-MUP-015'),

('Brightening Eye Cream', 'Bliss & Glow',
 'A caffeine-and-peptide eye cream that targets dark circles and puffiness.',
 1750.00, 25, 2, '/assets/images/products/16.jpeg', 'BNG-MST-016'),

('Exfoliating AHA/BHA Toner', 'Bliss & Glow',
 'A weekly chemical exfoliant that smooths texture and unclogs pores.',
 1550.00, 30, 1, '/assets/images/products/17.jpeg', 'BNG-CLN-017'),

('Body Glow Lotion', 'Bliss & Glow',
 'Silky body lotion infused with shea butter and a subtle shimmer.',
 1450.00, 40, 7, '/assets/images/products/18.jpeg', 'BNG-BDY-018'),

('Coffee Body Scrub', 'Bliss & Glow',
 'Energizing coffee-and-coconut body scrub for soft, glowing skin.',
 1250.00, 35, 7, '/assets/images/products/19.jpeg', 'BNG-BDY-019'),

('Lip Sleeping Mask — Berry', 'Bliss & Glow',
 'An overnight lip mask that wakes up lips soft, plump and pink.',
 1150.00, 45, 5, '/assets/images/products/20.jpeg', 'BNG-LIP-020');
