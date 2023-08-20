package com.fcom.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.fcom.model.sale;
import com.fcom.model.user;
@Repository
public interface SalesService {
    sale saveSale(sale sale);
    sale getSaleById(Long id);
    List<sale> getSalesForDate(String date); 
    public List<sale> getSalesData() ;
    public sale updateSale(sale sale);
    public List<sale> getSalesForDateAndSellerId(String date,Long id);
    public byte[] generateSalesReportForSeller(user seller) throws IOException;
    public void deleteSale(Long saleId) ;
    public List<sale> getSalesDataForSellerId(Long id) ;



}
