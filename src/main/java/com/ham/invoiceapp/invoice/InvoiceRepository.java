package com.ham.invoiceapp.invoice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository: DB와 커뮤니케이션하는 통로, Entity는 그때 쓰는 데이터 형태
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByVendor_StoreNameContaining(String vendorName);
}
