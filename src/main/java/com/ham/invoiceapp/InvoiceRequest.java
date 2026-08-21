package com.ham.invoiceapp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO: 클라이언트가 보내는 요청 데이터의 모양, Entity와 무관한 평범한 자바 클래스
public class InvoiceRequest {

    @NotBlank(message = "StoreName is mandatory")
    private String storeName;

    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "issuedAt is mandatory")
    private LocalDateTime issuedAt;

    @NotBlank(message = "Category is mandatory")
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
