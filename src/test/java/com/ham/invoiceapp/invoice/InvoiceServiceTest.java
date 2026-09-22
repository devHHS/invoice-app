package com.ham.invoiceapp.invoice;

import com.ham.invoiceapp.vendor.Vendor;
import com.ham.invoiceapp.vendor.VendorNotFoundException;
import com.ham.invoiceapp.vendor.VendorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// 레슨 0025 참고: VendorServiceTest와 같은 애노테이션 구조, 단 mock이 두 개다.
@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    // TODO: InvoiceService.java의 생성자를 보고, 받는 두 타입을 그대로 @Mock 필드로 선언하자.
    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    @Test
    void save_Vendor가_존재하면_저장하고_InvoiceResponse를_반환한다() {
        // Arrange
        // TODO 1: Vendor를 하나 준비하자 (id, storeName).
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        vendor.setStoreName("Starbucks");

        // TODO 2: vendorRepository.findById(vendorId)가 위 Vendor를 담은 Optional을 리턴하도록 stub하자.
        //         InvoiceService.toEntity()가 실제로 이 메서드를 부른다는 걸 InvoiceService.java에서 다시 확인.
        when(vendorRepository.findById(vendor.getId())).thenReturn(Optional.of(vendor));
        // TODO 3: InvoiceRequest를 준비하자 (vendorId, amount, issuedAt, category).
        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setVendorId(1L);
        invoiceRequest.setAmount(BigDecimal.valueOf(3));
        invoiceRequest.setCategory("Cafe");
        invoiceRequest.setIssuedAt(LocalDateTime.now());

        // TODO 4: invoiceRepository.save(...)를 stub하자.
        //         레슨 0025에서 확인한 이유로, 인자는 특정 Invoice 객체가 아니라 any(Invoice.class)를 쓴다.
        //         리턴값은 우리가 직접 만든 Invoice(id/vendor/amount/issuedAt/category 세팅)로 준다.
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setVendor(vendor);
        invoice.setAmount(BigDecimal.valueOf(3));
        invoice.setIssuedAt(LocalDateTime.now());
        invoice.setCategory("Cafe");

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        // Act
        // TODO: invoiceService.save(request) 호출.
        InvoiceResponse invoiceResponse = invoiceService.save(invoiceRequest);

        // Assert
        // TODO: result.getVendorName()이 실제 vendor.getStoreName()과 일치하는지 확인하자.
        //       (private인 toEntity/toResponse를 간접 검증하는 방법이다 — 레슨 0025 Q2 참고.)
        assertThat(invoiceResponse.getVendorName()).isEqualTo(vendor.getStoreName());
    }

    @Test
    void save_Vendor가_존재하지_않으면_VendorNotFoundException을_던진다() {
        // TODO: 힌트 없이 직접. vendorRepository.findById(...)가 Optional.empty()를 리턴하도록 stub하면
        //       toEntity() 안에서 무슨 일이 일어날지 예측해보고 작성하자.
        // Arrange
        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setVendorId(999L);
        when(vendorRepository.findById(999L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> invoiceService.save(invoiceRequest)).isInstanceOf(VendorNotFoundException.class);


    }

    @Test
    void findAll_검색어가_없으면_전체_목록을_반환한다() {
        // Arrange
        // TODO 1: Vendor 하나를 준비하자 (save() 테스트에서 했던 것과 같은 방식).
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        vendor.setStoreName("Starbucks");

        // TODO 2: Invoice 하나를 준비하자 (new Invoice() + setId/setVendor/setAmount/setIssuedAt/setCategory).
        //         이때 setVendor(...)를 빠뜨리면 안 된다 — toResponse()가 invoice.getVendor().getStoreName()을 부르기 때문에,
        //         vendor가 없는 Invoice를 넘기면 NullPointerException이 난다.
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setVendor(vendor);
        invoice.setAmount(BigDecimal.valueOf(3));
        invoice.setIssuedAt(LocalDateTime.now());
        invoice.setCategory("Cafe");

        // TODO 3: InvoiceService.findAll(null)을 실행하면 InvoiceRepository의 어떤 메서드가 불리는지
        //         InvoiceService.java 53~59번째 줄을 다시 읽고 확인하자. 그 메서드가 List.of(위에서 만든 Invoice)를 리턴하도록 stub하자.
        when(invoiceRepository.findAll()).thenReturn(List.of(invoice));

        // Act
        // TODO 4: invoiceService.findAll(null) 호출. (검색어 없음 = null)
        List<InvoiceResponse> result = invoiceService.findAll(null);

        // Assert
        // TODO 5: 결과 리스트 크기가 1인지 확인하자. (원한다면 결과의 vendorName도 같이 확인)
        assertThat(result).hasSize(1);

    }

    @Test
    void findAll_검색어가_있으면_필터링된_목록을_반환한다() {
        // 위 테스트와 구조는 같다. 딱 하나 다른 점: InvoiceRepository의 어떤 메서드를 stub해야 하는지
        // (InvoiceRepository.java에 findAll() 말고 메서드가 하나 더 있다) 와, Act에서 findAll(null) 대신
        // findAll("검색어")를 호출한다는 것.
    }

    @Test
    void findById_존재하면_InvoiceResponse를_반환한다() {
        // TODO: VendorServiceTest의 findById(정상) 패턴과 구조가 같다.
    }

    @Test
    void findById_존재하지_않으면_InvoiceNotFoundException을_던진다() {
        // TODO: VendorServiceTest의 findById(예외) 패턴과 구조가 같다.
    }

    // update()는 InvoiceService.java를 다시 보면 실패 지점이 두 곳이다:
    //   1. invoiceRepository.existsById(id)가 false — invoiceId 자체가 없음
    //   2. toEntity() 내부의 vendorRepository.findById(...)가 비어있음 — vendorId가 없음
    // 코드를 짜기 전에, 각 테스트가 이 둘 중 어디를 타게 만들지 먼저 정리해보고 시작하자 (레슨 0025 힌트 4).
    @Test
    void update_Invoice가_존재하지_않으면_InvoiceNotFoundException을_던진다() {
        // TODO:
    }

    @Test
    void update_Vendor가_존재하지_않으면_VendorNotFoundException을_던진다() {
        // TODO:
    }

    @Test
    void update_둘_다_존재하면_수정된_InvoiceResponse를_반환한다() {
        // TODO:
    }

    @Test
    void delete_존재하지_않으면_InvoiceNotFoundException을_던진다() {
        // TODO:
    }

    @Test
    void delete_존재하면_삭제한다() {
        // TODO: delete()는 void다 — verify(...)가 필요하다는 걸 Phase 1에서 이미 했다.
    }

}
