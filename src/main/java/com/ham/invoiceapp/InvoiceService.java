package com.ham.invoiceapp;

import org.springframework.stereotype.Service;

import java.util.List;

// Service: 트랜잭션 경계와 비즈니스 로직 담당, Controller와 Repository 사이를 중개
@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private Invoice toEntity(InvoiceRequest invoiceRequest) {
        Invoice invoice = new Invoice();
        invoice.setStoreName(invoiceRequest.getStoreName());
        invoice.setAmount(invoiceRequest.getAmount());
        invoice.setIssuedAt(invoiceRequest.getIssuedAt());
        invoice.setCategory(invoiceRequest.getCategory());
        return invoice;
    }

    private InvoiceResponse toResponse(Invoice invoice) {
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(invoice.getId());
        invoiceResponse.setStoreName(invoice.getStoreName());
        invoiceResponse.setAmount(invoice.getAmount());
        invoiceResponse.setIssuedAt(invoice.getIssuedAt());
        invoiceResponse.setCategory(invoice.getCategory());
        return invoiceResponse;
    }

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public InvoiceResponse save(InvoiceRequest invoiceRequest) {
        Invoice invoice = toEntity(invoiceRequest);
        return toResponse(invoiceRepository.save(invoice));
    }

    public List<InvoiceResponse> findAll() {
        // this::toResponse는 invoice -> toResponse(invoice)를 줄여 쓴 메서드 참조 —
        // 리스트의 Invoice 원소마다 toResponse를 호출해 InvoiceResponse로 바꾼다
        return invoiceRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public InvoiceResponse findById(Long id) {
        return toResponse(invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException(id)));
    }

    public InvoiceResponse update(Long id, InvoiceRequest invoiceRequest) {
        if (!invoiceRepository.existsById(id)) {
            throw new InvoiceNotFoundException(id);
        }

        Invoice invoice = toEntity(invoiceRequest);
        invoice.setId(id);
        return toResponse(invoiceRepository.save(invoice));
    }

    public void delete(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new InvoiceNotFoundException(id);
        }

        invoiceRepository.deleteById(id);
    }

}
