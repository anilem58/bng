package com.blissandglow.service;

import com.blissandglow.dao.CategoryDAO;
import com.blissandglow.dao.ProductDAO;
import com.blissandglow.model.Category;
import com.blissandglow.model.Product;
import com.blissandglow.util.ValidationUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProductService {

    private final ProductDAO productDAO   = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    public List<Product> getAll() throws SQLException {
        return productDAO.findAll();
    }

    public List<Product> getPaginated(int page, int pageSize, int categoryId, String sort) throws SQLException {
        int offset = (page - 1) * pageSize;
        return productDAO.findPaginated(offset, pageSize, categoryId, sort);
    }

    public int getTotalCount(int categoryId) throws SQLException {
        return productDAO.countProducts(categoryId);
    }

    public List<Product> getByCategories(int[] categoryIds) throws SQLException {
        return productDAO.findByCategories(categoryIds);
    }

    public List<Product> search(String keyword) throws SQLException {
        if (ValidationUtil.isBlank(keyword)) return getAll();
        return productDAO.search(keyword.trim());
    }

    public Product getById(int id) throws SQLException {
        return productDAO.findById(id);
    }

    /** Create a product. Returns null on success, or an error message. */
    public String addProduct(Product product) throws SQLException {
        String err = validate(product, 0);
        if (err != null) return err;
        productDAO.insert(product);
        return null;
    }

    /** Update a product. Returns null on success, or an error message. */
    public String updateProduct(Product product) throws SQLException {
        String err = validate(product, product.getProductId());
        if (err != null) return err;
        productDAO.update(product);
        return null;
    }

    public void deleteProduct(int id) throws SQLException {
        productDAO.delete(id);
    }

    private String validate(Product p, int excludeId) throws SQLException {
        if (ValidationUtil.isBlank(p.getName()))        return "Product name is required.";
        if (ValidationUtil.isBlank(p.getBrand()))       return "Brand is required.";
        if (ValidationUtil.isBlank(p.getSku()))         return "SKU is required.";
        if (p.getPrice() == null || p.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            return "Price must be greater than zero.";
        if (p.getStockQuantity() < 0)                   return "Stock quantity cannot be negative.";
        if (p.getCategoryId() <= 0)                     return "Please select a category.";
        if (productDAO.skuExists(p.getSku(), excludeId)) return "SKU already exists.";
        return null;
    }

    public int countProducts() throws SQLException { return productDAO.countAll(); }

    // ── Category management ───────────────────────────────────────────────

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.findAll();
    }

    public Category getCategoryById(int id) throws SQLException {
        return categoryDAO.findById(id);
    }

    public String addCategory(Category category) throws SQLException {
        if (ValidationUtil.isBlank(category.getName())) return "Category name is required.";
        if (categoryDAO.nameExists(category.getName(), 0)) return "Category name already exists.";
        categoryDAO.insert(category);
        return null;
    }

    public String updateCategory(Category category) throws SQLException {
        if (ValidationUtil.isBlank(category.getName())) return "Category name is required.";
        if (categoryDAO.nameExists(category.getName(), category.getCategoryId())) return "Category name already exists.";
        categoryDAO.update(category);
        return null;
    }

    public void deleteCategory(int id) throws SQLException {
        categoryDAO.delete(id);
    }

    // ── Report helpers ────────────────────────────────────────────────────

    public List<Object[]> getTopOrderedProducts(int limit) throws SQLException {
        return productDAO.getTopOrderedProducts(limit);
    }

    public List<Object[]> getSalesByCategory() throws SQLException {
        return productDAO.getSalesByCategory();
    }

    public List<Object[]> getStockReport() throws SQLException {
        return productDAO.getStockReport();
    }
}
