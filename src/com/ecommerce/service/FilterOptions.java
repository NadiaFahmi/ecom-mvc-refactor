package com.ecommerce.service;

import java.time.LocalDate;

public class FilterOptions {
    private String targetUserId;          // Used by Admin to filter by specific customer
    private String productId;             // Filter by product involved in the transaction
    private String status;                // "pending", "paid", "cancelled", etc.
    public LocalDate fromDate;           // Filter from this date onward
    public LocalDate toDate;             // Filter up to this date

    // Constructors
    public FilterOptions() {}

    public FilterOptions(String targetUserId, String productId, String status,
                         LocalDate fromDate, LocalDate toDate) {
        this.targetUserId = targetUserId;
        this.productId = productId;
        this.status = status;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    // Getters
    public String getTargetUserId() {
        return targetUserId;
    }

    public String getProductId() {
        return productId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    // Setters (optional if you prefer using constructor only)
    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}
