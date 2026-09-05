package com.ham.invoiceapp.vendor;


import org.springframework.stereotype.Service;

import java.util.List;

// Service: 트랜잭션 경계와 비즈니스 로직 담당, Controller와 Repository 사이를 중개
@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public Vendor save(Vendor vendor) {
        // TODO: InvoiceService.save()를 참고 — 변환 없이 그냥 저장하고 반환하면 된다
        return vendorRepository.save(vendor);
    }

    public List<Vendor> findAll() {
        // TODO: InvoiceService.findAll()의 검색어 없는 분기(vendorRepository.findAll())를 참고
        return vendorRepository.findAll();
    }

    public Vendor findById(Long id) {
        // TODO: 없으면 VendorNotFoundException을 던져야 한다 — InvoiceService.findById() 참고
        return vendorRepository.findById(id).orElseThrow(() -> new VendorNotFoundException(id));
    }

    public Vendor update(Long id, Vendor vendor) {
        // TODO: InvoiceService.update() 참고 — existsById로 확인 후 id를 세팅하고 저장
        if (!vendorRepository.existsById(id)) {
            throw new VendorNotFoundException(id);
        }
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    public void delete(Long id) {
        // TODO: InvoiceService.delete() 참고 — existsById로 확인 후 삭제
        if (!vendorRepository.existsById(id)) {
            throw new VendorNotFoundException(id);
        }
        vendorRepository.deleteById(id);
    }

}
