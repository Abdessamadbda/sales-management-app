package com.fcom.controller;
import com.fcom.model.sale;
import com.fcom.model.user;
import com.fcom.repository.SalesRepository;
import com.fcom.repository.UserRepository;
import com.fcom.service.SalesService;
import com.fcom.service.UserService;

import io.jsonwebtoken.io.IOException;
import org.springframework.http.MediaType;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Configuration
public class SalesController {
	@Autowired
 private final SalesService salesService;
	@Autowired
	 private final UserService userService;
    @Autowired
    public SalesController(SalesService salesService,UserService userService) {
        this.salesService = salesService;
        this.userService = userService;

    }


 @PostMapping("/seller/declaration")
 public ResponseEntity<sale> saveSale(@RequestBody sale sale) {
     sale savedSale = salesService.saveSale(sale);
     if (savedSale != null) {
         return ResponseEntity.ok(savedSale);
     } else {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     } 
 }
 @PostMapping("/seller/sales/update")
 public ResponseEntity<sale> updateSale(@RequestBody sale sale) {
     sale updatedSale = salesService.updateSale(sale);
     if (updatedSale != null) {
         return ResponseEntity.ok(updatedSale);
     } else {
         return ResponseEntity.notFound().build();
     }
 }
 @GetMapping("/today/{sellerId}")
 public ResponseEntity<List<sale>> getSalesForToday(@PathVariable Long sellerId) {
     LocalDate currentDate = LocalDate.now();
     List<sale> sales = salesService.getSalesForDateAndSellerId(currentDate.toString(),sellerId);
     return ResponseEntity.ok(sales);
 }
 @GetMapping(value = "/generate-sales-report/{sellerId}", produces = MediaType.APPLICATION_PDF_VALUE)
 public ResponseEntity<byte[]> generatePDFSalesReport(@PathVariable Long sellerId) throws IOException, java.io.IOException {
	 user seller = userService.getSellerInfo(sellerId);
	    if (seller == null) {
	        
	        return ResponseEntity.notFound().build();
	    }
	    byte[] pdfBytes = salesService.generateSalesReportForSeller(seller);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("attachment", "sales_report.pdf");

	    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK); }
 @GetMapping("/report")
 public ResponseEntity<List<Map<String, Object>>> generateSalesReport(@RequestParam(required = false) Long sellerId) throws java.io.IOException {
	 user seller = userService.getSellerInfo(sellerId);
     List<sale> salesData = salesService.getSalesData(); 
     LocalDate today = LocalDate.now();
     List<sale> filteredSalesData = salesData.stream()
             .filter(sale -> !sale.getDate().equals(today.toString()))
             .collect(Collectors.toList());
     if (sellerId != null  ) {
    	 filteredSalesData = filteredSalesData.stream()
    			    .filter(sale -> Objects.equals(sale.getSellerId(), sellerId))
    			    .collect(Collectors.toList());

     }
     
     List<Map<String, Object>> reports = new ArrayList<>();
     Map<String, List<sale>> salesByDate = groupSalesByDate(filteredSalesData, sellerId);
     for (Map.Entry<String, List<sale>> entry : salesByDate.entrySet()) {
         String date = entry.getKey();
         List<sale> salesForDate = entry.getValue();

         byte[] reportBytes = generateSalesReport(salesForDate,sellerId);
         String fileName = "Etat_de_Vente " + date + ".docx";

         Map<String, Object> reportInfo = new HashMap<>();
         reportInfo.put("fileName", fileName);
         reportInfo.put("fileBytes", reportBytes);

         reports.add(reportInfo);
     }
     return ResponseEntity.ok(reports);
 }



 private Map<String, List<sale>> groupSalesByDate(List<sale> salesData, Long  sellerId) {
	    Map<String, List<sale>> salesByDate = new LinkedHashMap<>();

	    for (sale sale : salesData) {
	         Long sid = (sale.getSellerId() != null) ? sale.getSellerId() : 0;
	        if ( sid == sellerId) {
	            String date = sale.getDate();
	            if (!salesByDate.containsKey(date)) {
	                salesByDate.put(date, new ArrayList<>());
	            }
	            salesByDate.get(date).add(sale);
	        }
	    }

	    return salesByDate;
	}

 private byte[] generateSalesReport(List<sale> salesData, Long sellerId) throws IOException, java.io.IOException {	    XWPFDocument document = new XWPFDocument();

 Map<String, List<sale>> salesByDate = groupSalesByDate(salesData, sellerId);
 user seller = userService.getSellerInfo(sellerId);
	    for (Map.Entry<String, List<sale>> entry : salesByDate.entrySet()) {
	        String date = entry.getKey();
	        List<sale> salesForDate = entry.getValue();

	        XWPFParagraph titleParagraph = document.createParagraph();
	        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
	        titleParagraph.setSpacingAfter(800);
	        XWPFRun titleRun = titleParagraph.createRun();
	        titleRun.setText("Etat de vente de " + date);
	        titleRun.setBold(true);
	        titleRun.setFontSize(20);

	        XWPFParagraph nomParagraph = document.createParagraph();
	        XWPFRun nomRun = nomParagraph.createRun();
	        nomRun.setText("Nom du vendeur: ");
	        nomRun.setBold(true);
	        XWPFRun nomValueRun = nomParagraph.createRun();
	        nomValueRun.setText(String.valueOf(seller.getNom_complet()));
	        nomParagraph.setSpacingAfter(0);
		     XWPFParagraph villeParagraph = document.createParagraph();
		     XWPFRun villeRun = villeParagraph.createRun();
		     villeRun.setText("Ville: ");
		     villeRun.setBold(true);
		     XWPFRun villeValueRun = villeParagraph.createRun();
		     villeValueRun.setText(String.valueOf(seller.getVille()));
	        
	        XWPFParagraph spacingParagraph = document.createParagraph();
	        XWPFParagraph spacingParagraph1 = document.createParagraph();

	        spacingParagraph.setSpacingAfter(60); 
	        spacingParagraph1.setSpacingAfter(60); 

	        XWPFTable table = document.createTable();
	        XWPFTableRow headerRow = table.getRow(0);
	        headerRow.addNewTableCell().setText("Produit");
	        headerRow.addNewTableCell().setText("Quantité");
	        headerRow.addNewTableCell().setText("Prix");
	        headerRow.addNewTableCell().setText("Num_téléphone");
	        headerRow.addNewTableCell().setText("IMEI");
	        headerRow.addNewTableCell().setText("Avec carte");

	        float totalPrix = 0;
	        float Recharge = 0; 
	        float Tpe = 0;
	        for (sale sale : salesForDate) {
	            if(!"recharge".equals(sale.getNom()) && !"tpe".equals(sale.getNom())) {
	            XWPFTableRow dataRow = table.createRow();
	            String type = (sale.getType() != null) ? String.valueOf(sale.getType()) : "";
	            dataRow.getCell(1).setText(type);
	            dataRow.getCell(2).setText(String.valueOf(sale.getQuantite()));
	            dataRow.getCell(3).setText(String.valueOf(sale.getPrix()));
	            dataRow.getCell(4).setText(String.valueOf(sale.getTelephone()));
	            dataRow.getCell(5).setText(String.valueOf(sale.getImei()));
	            dataRow.getCell(6).setText(String.valueOf(sale.getCard()));
	            totalPrix += sale.getPrix();
	            }
	            else {
	            	if ("recharge".equals(sale.getNom())) {
	                    Recharge = sale.getRecharge();
	                } else if ("tpe".equals(sale.getNom())) {
	                    Tpe = sale.getTpe();
	                }
	            }
	        }
	        XWPFParagraph spacingParagraph2 = document.createParagraph();
	        XWPFParagraph spacingParagraph3 = document.createParagraph();

	        spacingParagraph2.setSpacingBefore(50); 
	        spacingParagraph3.setSpacingBefore(50); 

	     XWPFParagraph totalParagraph = document.createParagraph();
	     XWPFRun totalRun = totalParagraph.createRun();
	     totalRun.setText("Total en DH: " + String.valueOf(totalPrix));
	     totalRun.setBold(true);
	     totalParagraph.setAlignment(ParagraphAlignment.CENTER);

	     XWPFParagraph rechargeParagraph = document.createParagraph();
	     XWPFRun rechargeRun = rechargeParagraph.createRun();
	     rechargeRun.setText("Recharge: " + String.valueOf(Recharge));
	     rechargeRun.setBold(true);
	     rechargeParagraph.setAlignment(ParagraphAlignment.CENTER);

	     XWPFParagraph tpeParagraph = document.createParagraph();
	     XWPFRun tpeRun = tpeParagraph.createRun();
	     tpeRun.setText("Tpe: " + String.valueOf(Tpe));
	     tpeRun.setBold(true);
	     tpeParagraph.setAlignment(ParagraphAlignment.CENTER);

	     int[] columnWidths = { 0, 2000, 1500, 1700, 2400, 2700,2000 }; 

	     for (int colIndex = 0; colIndex < columnWidths.length; colIndex++) {
	         int width = columnWidths[colIndex];
	         
	         for (XWPFTableRow row : table.getRows()) {
	             if (row.getTableCells().size() > colIndex) {
	                 XWPFTableCell cell = row.getCell(colIndex);
	         
	                 if (cell != null) {
	                     CTTcPr tcPr = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr() : cell.getCTTc().addNewTcPr();
	                     CTTblWidth cellWidth = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
	                     cellWidth.setType(STTblWidth.DXA); 
	                     cellWidth.setW(BigInteger.valueOf(width)); 
	         
	                 }
	             }
	         }
	     }
	    
	     for (XWPFTableRow row : table.getRows()) {
	    	    for (XWPFTableCell cell : row.getTableCells()) {
	    	        if (cell.getParagraphs().isEmpty()) {
	    	            cell.addParagraph();
	    	        }

	    	        for (XWPFParagraph paragraph : cell.getParagraphs()) {
	    	            paragraph.setAlignment(ParagraphAlignment.CENTER);
	    	        }
	    	    }
	    	}
	     document.createParagraph();

	    }

	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    document.write(outputStream);
	    document.close();

	    return outputStream.toByteArray();
	}
 @GetMapping("/sales-report")
 public ResponseEntity<List<Map<String, Object>>> generateSalesReport1(@RequestParam(required = false) Long sellerId) throws IOException, java.io.IOException {
	 user seller = userService.getSellerInfo(sellerId);
     List<sale> salesData = salesService.getSalesDataForSellerId(sellerId);
     Map<String, Map<String, List<sale>>> groupedSales = new LinkedHashMap<>();
     for (sale sale : salesData) {
         String date = sale.getDate();
         String sellerName = seller.getNom_complet();
         if (!groupedSales.containsKey(date)) {
             groupedSales.put(date, new LinkedHashMap<>());
         }
         if (!groupedSales.get(date).containsKey(sellerName)) {
             groupedSales.get(date).put(sellerName, new ArrayList<>());
         
         groupedSales.get(date).get(sellerName).add(sale);
         }
     }

     List<Map<String, Object>> reports = new ArrayList<>();
     for (Map.Entry<String, Map<String, List<sale>>> dateEntry : groupedSales.entrySet()) {
         String date = dateEntry.getKey();
         Map<String, List<sale>> salesBySeller = dateEntry.getValue();

         for (Map.Entry<String, List<sale>> sellerEntry : salesBySeller.entrySet()) {
             String sellerName = sellerEntry.getKey();
             List<sale> salesForSeller = sellerEntry.getValue();

             byte[] reportBytes = generateSalesReportPDF(date, seller, salesForSeller);

             String fileName = "Etat_de_vente_" + sellerName + "_" + date + ".pdf";

             Map<String, Object> reportInfo = new HashMap<>();
             reportInfo.put("fileName", fileName);
             reportInfo.put("fileBytes", reportBytes);

             reports.add(reportInfo);
         }
     }

     return ResponseEntity.ok(reports);
 }

 private byte[] generateSalesReportPDF(String date, user seller, List<sale> salesData) throws IOException, java.io.IOException {
     PDDocument document = new PDDocument();
     PDPage page = new PDPage();
     document.addPage(page);
     
     PDPageContentStream contentStream = new PDPageContentStream(document, page);

     float margin = 50;
     float margin1 = 10;

     float yStart = page.getMediaBox().getHeight() - margin;
     float yStart1 = yStart-60;
     float yStart2 = yStart1-15;

     float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
     float yPosition = yStart2 -100;
     int rowsPerPage = 10;

     float tableHeight = 20 * rowsPerPage;
     float bottomMargin = -30;
     float yPositionStart = yStart - tableHeight - bottomMargin;

     contentStream.setFont(PDType1Font.HELVETICA_BOLD, 15); 
     contentStream.setNonStrokingColor(Color.ORANGE); 

     contentStream.beginText();
     contentStream.newLineAtOffset(margin, yStart);
     contentStream.showText("Etat de vente du: " + date);
     contentStream.endText();
     contentStream.setNonStrokingColor(Color.BLACK);
     contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
     contentStream.beginText();
     contentStream.newLineAtOffset(margin, yStart1);
     contentStream.showText("Nom du vendeur : " );
     contentStream.setFont(PDType1Font.HELVETICA, 10); 
     contentStream.showText(seller.getNom_complet());
     contentStream.endText();
     contentStream.beginText();
     contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10); 
     contentStream.newLineAtOffset(margin, yStart2);
     contentStream.showText("Ville: ");
     contentStream.setFont(PDType1Font.HELVETICA, 10); 
     contentStream.showText(seller.getVille()); 
     contentStream.endText();

     float tableYPosition = yPosition;
     float Recharge = 0; 
     float Tpe = 0;
     contentStream.beginText();
     contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
     contentStream.newLineAtOffset(margin1, tableYPosition);
     contentStream.showText("Produit");
     contentStream.newLineAtOffset(tableWidth * 0.32f, 0);
     contentStream.showText("Quantité");
     contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
     contentStream.showText("Prix");
     contentStream.newLineAtOffset(tableWidth * 0.14f, 0);
     contentStream.showText("Num_téléphone");
     contentStream.newLineAtOffset(tableWidth * 0.23f, 0);
     contentStream.showText("IMEI");
     contentStream.newLineAtOffset(tableWidth * 0.17f, 0);
     contentStream.showText("Avec carte");
     
     contentStream.endText();
     
     float totalPrice = 0;
     boolean isGrayRow = true;
     for (sale sale : salesData) {
         tableYPosition -= 15;
         if (isGrayRow) {
             contentStream.setNonStrokingColor(Color.LIGHT_GRAY);
         } else {
             contentStream.setNonStrokingColor(Color.WHITE);
         }
         if(!"recharge".equals(sale.getNom()) && !"tpe".equals(sale.getNom())) {
        	 contentStream.fillRect(margin1, tableYPosition - 16, tableWidth+72, 15);
             isGrayRow = !isGrayRow;

             contentStream.setNonStrokingColor(Color.BLACK);
         contentStream.beginText();
         contentStream.setFont(PDType1Font.HELVETICA, 10);
         contentStream.newLineAtOffset(margin1, tableYPosition-12);
         String type = (sale.getType() != null) ? String.valueOf(sale.getType()) : "";
         contentStream.showText(type);
         contentStream.newLineAtOffset(tableWidth * 0.32f, 0);
         contentStream.showText(String.valueOf(sale.getQuantite()));
         contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
         contentStream.showText(String.valueOf(sale.getPrix()));
         contentStream.newLineAtOffset(tableWidth * 0.14f, 0);
         contentStream.showText(String.valueOf(sale.getTelephone()));
         contentStream.newLineAtOffset(tableWidth * 0.23f, 0);
         contentStream.showText(String.valueOf(sale.getImei()));
         contentStream.newLineAtOffset(tableWidth * 0.17f, 0);
         contentStream.showText(String.valueOf(sale.getCard()));
         contentStream.endText();
         totalPrice += sale.getPrix();
         }
         else {
        	 if ("recharge".equals(sale.getNom())) {
                 Recharge = sale.getRecharge();
             } else if ("tpe".equals(sale.getNom())) {
                 Tpe = sale.getTpe();
             }
         }
     }
     contentStream.setNonStrokingColor(Color.BLACK); // Set the text color

     tableYPosition -= 70;
     contentStream.beginText();
     contentStream.newLineAtOffset(margin, tableYPosition+18); 
     contentStream.setFont(PDType1Font.HELVETICA, 12);
     contentStream.showText("Total en DH:");
     contentStream.endText();

    
     contentStream.beginText();
     contentStream.newLineAtOffset(tableWidth * 0.66f, tableYPosition+18); 
     contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
     contentStream.showText(String.valueOf(totalPrice));
     contentStream.endText();
     tableYPosition -= 15;
     contentStream.beginText();
     contentStream.newLineAtOffset(margin, tableYPosition+18); 
     contentStream.setFont(PDType1Font.HELVETICA, 12);
     contentStream.showText("Recharge:");
     contentStream.endText();

     contentStream.beginText();
     contentStream.newLineAtOffset(tableWidth * 0.66f, tableYPosition+18); 
     contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
     contentStream.showText(String.valueOf(Recharge));
     contentStream.endText();

     tableYPosition -= 15;
     contentStream.beginText();
     contentStream.newLineAtOffset(margin, tableYPosition+18); 
     contentStream.setFont(PDType1Font.HELVETICA, 12);
     contentStream.showText("TPE:");
     contentStream.endText();

     contentStream.beginText();
     contentStream.newLineAtOffset(tableWidth * 0.66f, tableYPosition+18); 
     contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
     contentStream.showText(String.valueOf(Tpe));
     contentStream.endText();
     contentStream.close();

     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     document.save(outputStream);
     document.close();

     return outputStream.toByteArray();
 }
 @DeleteMapping("/seller/declaration/{saleId}")
 public ResponseEntity<?> deleteSale(@PathVariable Long saleId) {
     try {
         salesService.deleteSale(saleId);
         return ResponseEntity.noContent().build();
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting sale");
     }
 }

}

