package com.ham.invoiceapp.invoice;

import com.ham.invoiceapp.vendor.Vendor;
import com.ham.invoiceapp.vendor.VendorNotFoundException;
import com.ham.invoiceapp.vendor.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Service: 트랜잭션 경계와 비즈니스 로직 담당, Controller와 Repository 사이를 중개
@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final VendorRepository vendorRepository;

    private Invoice toEntity(InvoiceRequest invoiceRequest) {
        Invoice invoice = new Invoice();
        invoice.setAmount(invoiceRequest.getAmount());
        invoice.setIssuedAt(invoiceRequest.getIssuedAt());
        invoice.setCategory(invoiceRequest.getCategory());

        Vendor vendor = vendorRepository.findById(invoiceRequest.getVendorId())
                .orElseThrow(() -> new VendorNotFoundException(invoiceRequest.getVendorId()));
        invoice.setVendor(vendor);

        return invoice;
    }

    private InvoiceResponse toResponse(Invoice invoice) {
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(invoice.getId());
        invoiceResponse.setAmount(invoice.getAmount());
        invoiceResponse.setIssuedAt(invoice.getIssuedAt());
        invoiceResponse.setCategory(invoice.getCategory());

        invoiceResponse.setVendorName(invoice.getVendor().getStoreName());

        return invoiceResponse;

    }

    public InvoiceService(InvoiceRepository invoiceRepository, VendorRepository vendorRepository) {
        this.invoiceRepository = invoiceRepository;
        this.vendorRepository = vendorRepository;
    }

    public InvoiceResponse save(InvoiceRequest invoiceRequest) {
        Invoice invoice = toEntity(invoiceRequest);
        return toResponse(invoiceRepository.save(invoice));
    }

    public List<InvoiceResponse> findAll(String vendorName) {
        // vendorId 필수화와는 무관한 정상 분기 — 검색어(vendorName)를 안 보내면 전체 조회, 보내면 그 값으로 필터링
        if (vendorName == null) {
            return invoiceRepository.findAll()
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }
        // this::toResponse는 invoice -> toResponse(invoice)를 줄여 쓴 메서드 참조 —
        // 리스트의 Invoice 원소마다 toResponse를 호출해 InvoiceResponse로 바꾼다
        return invoiceRepository.findAllByVendor_StoreNameContaining(vendorName)
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
