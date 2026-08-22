package com.ham.invoiceapp;

import org.springframework.data.jpa.repository.JpaRepository;

// Repository: DB와 커뮤니케이션하는 통로, Entity는 그때 쓰는 데이터 형태
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
