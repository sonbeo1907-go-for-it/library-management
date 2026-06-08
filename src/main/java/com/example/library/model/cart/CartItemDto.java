package com.example.library.model.cart;

import java.math.BigDecimal;

public class CartItemDto {
    private int itemId;
    private int bookId;
    private String bookTitle;
    private String bookAuthor;
    private String imageFilename;
    private int quantity;
    private BigDecimal feePerItem;
    private String formattedFeePerItem;

    public CartItemDto() {}

    public CartItemDto(int itemId, int bookId, String bookTitle, String bookAuthor,
                       String imageFilename, int quantity, BigDecimal feePerItem) {
        this.itemId = itemId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.imageFilename = imageFilename;
        this.quantity = quantity;
        this.feePerItem = feePerItem;
    }

    // Getters and Setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }
    public String getImageFilename() { return imageFilename; }
    public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getFeePerItem() { return feePerItem; }
    public void setFeePerItem(BigDecimal feePerItem) { this.feePerItem = feePerItem; }

    public String getFormattedFeePerItem() {
        return formattedFeePerItem;
    }

    public void setFormattedFeePerItem(String formattedFeePerItem) {
        this.formattedFeePerItem = formattedFeePerItem;
    }
}