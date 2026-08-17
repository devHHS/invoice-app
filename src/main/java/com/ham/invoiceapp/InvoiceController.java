package com.ham.invoiceapp;

import org.springframework.web.bind.annotation.*;
import java.util.List;

// Controller: HTTP 요청/응답 처리 담당, 실제 로직은 Service에 위임
@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public Invoice save(@RequestBody Invoice invoice) {
        return invoiceService.save(invoice);
    }

    @GetMapping
    public List<Invoice> findAll() {
        return invoiceService.findAll();
    }

    @GetMapping("/{id}")
    public Invoice findById(@PathVariable Long id) {
        return invoiceService.findById(id);
    }

    @PutMapping("/{id}")
    public Invoice update(@PathVariable Long id, @RequestBody Invoice invoice) {
        return invoiceService.update(id, invoice);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        invoiceService.delete(id);
    }

}