package com.ham.invoiceapp.vendor;

public class VendorNotFoundException extends RuntimeException {

    public VendorNotFoundException(Long id) {
        super("Vendor with id " + id + " not found");
    }

}
