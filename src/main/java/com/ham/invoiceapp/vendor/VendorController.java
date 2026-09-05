package com.ham.invoiceapp.vendor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller: HTTP 요청/응답 처리 담당, 실제 로직은 Service에 위임
@RestController
@RequestMapping("/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    // TODO: POST /vendors — InvoiceController.save()의 @PostMapping 패턴 참고
    @PostMapping
    public Vendor save(@RequestBody Vendor vendor) {
        return vendorService.save(vendor);
    }

    // TODO: GET /vendors — 전체 조회, @GetMapping 패턴 참고 (검색 파라미터는 필요 없음)
    @GetMapping
    public List<Vendor> findAll() {
        return vendorService.findAll();
    }

    // TODO: GET /vendors/{id} — 단건 조회
    @GetMapping("/{id}")
    public Vendor findById(@PathVariable Long id) {
        return vendorService.findById(id);
    }

    // TODO: PUT /vendors/{id} — 수정
    @PutMapping("/{id}")
    public Vendor update(@PathVariable Long id, @RequestBody Vendor vendor) {
        return vendorService.update(id, vendor);
    }

    // TODO: DELETE /vendors/{id} — 삭제
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        vendorService.delete(id);
    }

}
