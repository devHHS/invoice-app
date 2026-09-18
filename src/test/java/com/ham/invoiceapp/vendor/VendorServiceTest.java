package com.ham.invoiceapp.vendor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// TODO: 이 클래스가 Spring 컨테이너 없이 순수 Java로 실행되는 "단위 테스트"가 되게 하려면
//       클래스 선언 위에 애노테이션이 하나 필요하다 — reference/0016 "애노테이션 3개" 참고.
@ExtendWith(MockitoExtension.class)
class VendorServiceTest {

    // TODO: VendorRepository를 진짜 대신 가짜(mock)로 만든다. 필드 위에 애노테이션 하나만 붙이면 된다.
    @Mock
    private VendorRepository vendorRepository;

    // TODO: 위 mock을 생성자에 자동으로 주입받아 VendorService 인스턴스를 만든다.
    //       VendorService의 생성자가 파라미터를 몇 개, 어떤 타입으로 받는지 VendorService.java에서 먼저 확인해보자.
    @InjectMocks
    private VendorService vendorService;

    @Test
    void save_저장한_Vendor를_그대로_반환한다() {
        // Arrange
        Vendor vendor = new Vendor();
        vendor.setStoreName("교보문고");

        // TODO: vendorRepository.save(vendor)가 호출되면 vendor 자신을 그대로 리턴하도록 설정해보자.
        //       (VendorService.save()가 실제로 vendorRepository.save(...)의 리턴값을 그대로 돌려주는지
        //        VendorService.java에서 다시 확인한 뒤 어떤 값을 리턴하게 할지 정하자)
        when(vendorRepository.save(vendor)).thenReturn((vendor));

        // Act
        // TODO: vendorService.save(vendor)를 호출한 결과를 변수에 담자.
        Vendor result = vendorService.save(vendor);

        // Assert
        // TODO: 결과의 storeName이 "교보문고"인지 확인하자.
        assertThat(result.getStoreName()).isEqualTo("교보문고");
    }

    @Test
    void findAll_전체_목록을_그대로_반환한다() {
        // Arrange
        Vendor vendor1 = new Vendor();
        vendor1.setStoreName("교보문고");
        Vendor vendor2 = new Vendor();
        vendor2.setStoreName("영풍문고");

        // TODO: vendorRepository.findAll()이 List.of(vendor1, vendor2)를 리턴하도록 설정.
        when(vendorRepository.findAll()).thenReturn(List.of(vendor1,vendor2));

        // Act
        // TODO: vendorService.findAll() 호출.
        List<Vendor> result = vendorService.findAll();

        // Assert
        // TODO: 결과 리스트의 크기가 2인지, 내용이 일치하는지 확인.
        //       (assertThat(result).hasSize(2) 또는 assertThat(result).containsExactly(vendor1, vendor2))
        assertThat(result).hasSize(2);
    }

    @Test
    void findById_존재하면_해당_Vendor를_반환한다() {
        // Arrange
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        vendor.setStoreName("교보문고");

        // TODO: vendorRepository.findById(1L)이 Optional.of(vendor)를 리턴하도록 설정.
        //       VendorRepository의 findById가 어떤 타입을 리턴하는지 JpaRepository 시그니처를 떠올려보자.
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));

        // Act
        // TODO: vendorService.findById(1L) 호출.
        Vendor result = vendorService.findById(1L);

        // Assert
        // TODO: 결과가 vendor와 같은지 확인.
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_존재하지_않으면_VendorNotFoundException을_던진다() {
        // Arrange
        // TODO: vendorRepository.findById(999L)이 Optional.empty()를 리턴하도록 설정.
        when(vendorRepository.findById(999L)).thenReturn(Optional.empty());

        // Act + Assert
        // TODO: vendorService.findById(999L)을 호출하면 VendorNotFoundException이 던져지는지 확인.
        //       assertThatThrownBy(() -> ...) 형태 — reference/0016 참고.
        //       VendorService.findById()의 orElseThrow(...) 코드를 다시 보고, 어떤 조건에서 이 예외가 나오는지 짚어보자.
        assertThatThrownBy(()-> vendorService.findById(999L)).isInstanceOf(VendorNotFoundException.class);
    }

    // TODO: update()도 같은 AAA 패턴으로 직접 작성해보기. 존재하는 id / 존재하지 않는 id, 두 경우 모두 다뤄야 한다.
    //       VendorService.update()가 존재 여부를 어떻게 확인하는지(existsById) 다시 보고,
    //       그 메서드 호출 결과를 mock으로 어떻게 설정할지 생각해보자.
    @Test
    void update_존재하지_않으면_VendorNotFoundException을_던진다(){
        // Arrange
        when(vendorRepository.existsById(999L)).thenReturn(false);
        Vendor vendor = new Vendor();

        // Act + Assert
        assertThatThrownBy(()-> vendorService.update(999L, vendor)).isInstanceOf(VendorNotFoundException.class);

    }

    @Test
    void  update_존재하면_수정된_Vendor를_반환한다(){
        // Arrange
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        vendor.setStoreName("Starbucks");

        when(vendorRepository.existsById(1L)).thenReturn(true);

        vendor.setStoreName("orSlow");
        when(vendorRepository.save(vendor)).thenReturn(vendor);

        // Act
        Vendor result = vendorService.update(1L, vendor);

        // Assert
        assertThat(result.getStoreName()).isEqualTo("orSlow");
        assertThat(result.getId()).isEqualTo(1L);
    }


    // TODO: delete()도 같은 패턴으로. update()와 마찬가지로 존재/미존재 두 경우.
    //       delete()는 리턴값이 없다(void) — 결과값을 assert하는 대신 verify(...)로
    //       "그 메서드가 실제로 호출됐는지"를 확인해야 한다. reference/0016의 verify 설명 참고.
    @Test
    void delete_존재하지_않으면_exception(){
        // Arrange
        when(vendorRepository.existsById(999L)).thenReturn(false);

        // Act + Assert
        assertThatThrownBy(()-> vendorService.delete(999L)).isInstanceOf(VendorNotFoundException.class);

    }


    @Test
    void delete_존재하면_삭제(){
        //Arrange
        when(vendorRepository.existsById(1L)).thenReturn(true);

        //Act
        vendorService.delete(1L);

        //Assert
        verify(vendorRepository).deleteById(1L);

    }


}
