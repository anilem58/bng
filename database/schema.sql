-- =====================================================================
-- Bliss and Glow Database Schema
-- CS5054NP Advanced Programming and Technologies Coursework
-- Run this FIRST in phpMyAdmin (XAMPP) before seed.sql
-- =====================================================================

DROP DATABASE IF EXISTS blissandglow_db;
CREATE DATABASE blissandglow_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE blissandglow_db;

-- ---------------------------------------------------------------------
-- Table: users
-- Stores both admin and customer accounts. New customers start as
-- PENDING and must be approved by an admin before they can log in.
-- ---------------------------------------------------------------------
CREATE TABLE users (
    user_id        INT AUTO_INCREMENT PRIMARY KEY,
    full_name      VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL UNIQUE,
    phone          VARCHAR(20)  NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    dob            DATE,
    address        VARCHAR(255),
    role           ENUM('ADMIN','CUSTOMER') NOT NULL DEFAULT 'CUSTOMER',
    status         ENUM('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_users_email (email),
    INDEX idx_users_status (status)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: categories
-- ---------------------------------------------------------------------
CREATE TABLE categories (
    category_id    INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(80) NOT NULL UNIQUE,
    description    VARCHAR(255)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: products
-- ---------------------------------------------------------------------
CREATE TABLE products (
    product_id     INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(150) NOT NULL,
    brand          VARCHAR(100) NOT NULL,
    description    TEXT,
    price          DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    category_id    INT NOT NULL,
    image_path     VARCHAR(255),
    sku            VARCHAR(50) NOT NULL UNIQUE,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES categories(category_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_products_category (category_id),
    INDEX idx_products_brand (brand)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: orders
-- ---------------------------------------------------------------------
CREATE TABLE orders (
    order_id        INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    order_date      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount    DECIMAL(10,2) NOT NULL CHECK (total_amount >= 0),
    status          ENUM('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED')
                       NOT NULL DEFAULT 'PENDING',
    shipping_address VARCHAR(255) NOT NULL,
    CONSTRAINT fk_order_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_orders_user (user_id),
    INDEX idx_orders_status (status)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: order_items (line items per order)
-- ---------------------------------------------------------------------
CREATE TABLE order_items (
    order_item_id  INT AUTO_INCREMENT PRIMARY KEY,
    order_id       INT NOT NULL,
    product_id     INT NOT NULL,
    quantity       INT NOT NULL CHECK (quantity > 0),
    unit_price     DECIMAL(10,2) NOT NULL CHECK (unit_price >= 0),
    CONSTRAINT fk_oi_order
        FOREIGN KEY (order_id) REFERENCES orders(order_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_oi_product
        FOREIGN KEY (product_id) REFERENCES products(product_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_oi_order (order_id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: wishlist
-- ---------------------------------------------------------------------
CREATE TABLE wishlist (
    wishlist_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_id        INT NOT NULL,
    product_id     INT NOT NULL,
    added_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_wishlist_user_product (user_id, product_id),
    CONSTRAINT fk_wl_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_wl_product
        FOREIGN KEY (product_id) REFERENCES products(product_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: contact_messages (from public Contact form)
-- ---------------------------------------------------------------------
CREATE TABLE contact_messages (
    message_id     INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL,
    subject        VARCHAR(200) NOT NULL,
    message        TEXT NOT NULL,
    submitted_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_read        BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_contact_isread (is_read)
) ENGINE=InnoDB;
