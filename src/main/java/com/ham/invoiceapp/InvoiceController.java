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
        Invoice invoice = new Invoice();
        invoice.setStoreName(invoiceRequest.getStoreName());
        invoice.setAmount(invoiceRequest.getAmount());
        invoice.setIssuedAt(invoiceRequest.getIssuedAt());
        invoice.setCategory(invoiceRequest.getCategory());

        Invoice savedInvoice = invoiceService.save(invoice);
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(savedInvoice.getId());
        invoiceResponse.setStoreName(savedInvoice.getStoreName());
        invoiceResponse.setAmount(savedInvoice.getAmount());
        invoiceResponse.setIssuedAt(savedInvoice.getIssuedAt());
        invoiceResponse.setCategory(savedInvoice.getCategory());

        return invoiceResponse;
    }

    @GetMapping
    public List<InvoiceResponse> findAll() {
        return invoiceService.findAll()
                .stream()
                .map(invoice -> {
                    InvoiceResponse invoiceResponse = new InvoiceResponse();
                    invoiceResponse.setId(invoice.getId());
                    invoiceResponse.setStoreName(invoice.getStoreName());
                    invoiceResponse.setAmount(invoice.getAmount());
                    invoiceResponse.setIssuedAt(invoice.getIssuedAt());
                    invoiceResponse.setCategory(invoice.getCategory());

                    return invoiceResponse;
                })
                .toList();
    }

    @GetMapping("/{id}")
    public InvoiceResponse findById(@PathVariable Long id) {
        Invoice invoice = invoiceService.findById(id);
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(invoice.getId());
        invoiceResponse.setStoreName(invoice.getStoreName());
        invoiceResponse.setAmount(invoice.getAmount());
        invoiceResponse.setIssuedAt(invoice.getIssuedAt());
        invoiceResponse.setCategory(invoice.getCategory());

        return invoiceResponse;
    }

    @PutMapping("/{id}")
    public InvoiceResponse update(@PathVariable Long id, @RequestBody InvoiceRequest invoiceRequest) {
        Invoice invoice = new Invoice();
        invoice.setStoreName(invoiceRequest.getStoreName());
        invoice.setAmount(invoiceRequest.getAmount());
        invoice.setIssuedAt(invoiceRequest.getIssuedAt());
        invoice.setCategory(invoiceRequest.getCategory());

        Invoice updatedInvoice = invoiceService.update(id, invoice);
        InvoiceResponse invoiceResponse = new  InvoiceResponse();
        invoiceResponse.setId(updatedInvoice.getId());
        invoiceResponse.setStoreName(updatedInvoice.getStoreName());
        invoiceResponse.setAmount(updatedInvoice.getAmount());
        invoiceResponse.setIssuedAt(updatedInvoice.getIssuedAt());
        invoiceResponse.setCategory(updatedInvoice.getCategory());

        return invoiceResponse;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        invoiceService.delete(id);
    }

}