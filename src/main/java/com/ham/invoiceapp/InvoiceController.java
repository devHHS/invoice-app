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
    public InvoiceResponse save(@RequestBody InvoiceRequest invoiceRequest) {
        return invoiceService.save(invoiceRequest);
    }

    @GetMapping
    public List<InvoiceResponse> findAll() {
        return invoiceService.findAll();
    }

    @GetMapping("/{id}")
    public InvoiceResponse findById(@PathVariable Long id) {
        return invoiceService.findById(id);
    }

    @PutMapping("/{id}")
    public InvoiceResponse update(@PathVariable Long id, @RequestBody InvoiceRequest invoiceRequest) {
        return invoiceService.update(id, invoiceRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        invoiceService.delete(id);
    }

}