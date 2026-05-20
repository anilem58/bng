package com.blissandglow.service;

import com.blissandglow.dao.WishlistDAO;
import com.blissandglow.model.WishlistItem;

import java.sql.SQLException;
import java.util.List;

public class WishlistService {

    private final WishlistDAO wishlistDAO = new WishlistDAO();

    public List<WishlistItem> getWishlist(int userId) throws SQLException {
        return wishlistDAO.findByUserId(userId);
    }

    public void toggle(int userId, int productId) throws SQLException {
        if (wishlistDAO.exists(userId, productId)) {
            wishlistDAO.remove(userId, productId);
        } else {
            wishlistDAO.add(userId, productId);
        }
    }

    public void add(int userId, int productId) throws SQLException {
        wishlistDAO.add(userId, productId);
    }

    public void remove(int userId, int productId) throws SQLException {
        wishlistDAO.remove(userId, productId);
    }

    public boolean isInWishlist(int userId, int productId) throws SQLException {
        return wishlistDAO.exists(userId, productId);
    }
}
