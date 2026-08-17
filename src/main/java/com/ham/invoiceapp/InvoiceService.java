package com.ham.invoiceapp;

import org.springframework.stereotype.Service;

import java.util.List;

// Service: 트랜잭션 경계와 비즈니스 로직 담당, Controller와 Repository 사이를 중개
@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public Invoice findById(Long id) {
        return invoiceRepository.findById(id).orElseThrow();
    }

    public Invoice update(Long id, Invoice invoice) {
        invoice.setId(id);
        return invoiceRepository.save(invoice);
    }

    public void delete(Long id) {
        invoiceRepository.deleteById(id);
    }

}
