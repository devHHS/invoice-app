package com.ham.invoiceapp.vendor;

import jakarta.persistence.*;

// Entity: DB 테이블과 직접 매핑되는 객체, JPA/Hibernate가 관리
@Entity
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeName;

    // reference/0001의 "no-args 생성자가 필요한 이유"를 다시 떠올려보자 — Hibernate가 왜 이걸 요구했는지.
    public Vendor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
