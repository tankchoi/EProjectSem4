package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.aptech.java.models.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}