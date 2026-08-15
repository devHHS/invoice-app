package com.ham.invoiceapp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvoiceRequest {

    private String storeName;

    private BigDecimal amount;

    private LocalDateTime issuedAt;

    private String category;

    public InvoiceRequest() {
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
