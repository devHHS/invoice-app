package com.ham.invoiceapp.invoice;

class InvoiceNotFoundException extends RuntimeException {

    public InvoiceNotFoundException(Long id) {
        super("Invoice with id " + id + " not found");
    }

}
