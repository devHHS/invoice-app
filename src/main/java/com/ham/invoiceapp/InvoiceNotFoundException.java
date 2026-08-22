package com.ham.invoiceapp;

public class InvoiceNotFoundException extends RuntimeException {

    public InvoiceNotFoundException(Long id) {
        super("Invoice with id " + id + " not found");
    }

}
