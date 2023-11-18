package com.fcom.controller;

import com.fcom.model.recharge;
import com.fcom.model.sale;
import com.fcom.model.tpe;
import com.fcom.model.user;
import com.fcom.repository.SalesRepository;
import com.fcom.repository.UserRepository;
import com.fcom.service.RechargeService;
import com.fcom.service.SalesService;
import com.fcom.service.TpeService;
import com.fcom.service.UserService;

import io.jsonwebtoken.io.IOException;
import jakarta.persistence.criteria.CriteriaBuilder.In;

import org.springframework.http.MediaType;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    private final RechargeService rechargeService;
    @Autowired
    private final TpeService tpeService;

    @Autowired
    public SalesController(SalesService salesService, UserService userService, RechargeService rechargeService,
            TpeService tpeService) {
        this.salesService = salesService;
        this.userService = userService;
        this.rechargeService = rechargeService;
        this.tpeService = tpeService;
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
        List<sale> sales = salesService.getSalesForDateAndSellerId(currentDate.toString(), sellerId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping(value = "/generate-sales-report/{sellerId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePDFSalesReport(@PathVariable Long sellerId)
            throws IOException, java.io.IOException {
        user seller = userService.getSellerInfo(sellerId);
        if (seller == null) {

            return ResponseEntity.notFound().build();
        }
        byte[] pdfBytes = salesService.generateSalesReportForSeller(seller);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "sales_report.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<List<Map<String, Object>>> generateSalesReport(@RequestParam(required = false) Long sellerId)
            throws java.io.IOException {
        user seller = userService.getSellerInfo(sellerId);
        List<sale> salesData = salesService.getSalesData();
        List<recharge> rechargeData = rechargeService.getRechargeData();
        List<tpe> tpeData = tpeService.getTpeData();
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minus(30, ChronoUnit.DAYS);
        List<sale> filteredSalesData = salesData.stream()
                .filter(sale -> !sale.getDate().equals(today.toString()))
                .filter(sale -> LocalDate.parse(sale.getDate()).isAfter(thirtyDaysAgo))
                .collect(Collectors.toList());
        if (sellerId != null) {
            filteredSalesData = filteredSalesData.stream()
                    .filter(sale -> Objects.equals(sale.getSellerId(), sellerId))
                    .collect(Collectors.toList());

        }
        List<recharge> filteredRechargeData = rechargeData.stream()
                .filter(recharge -> !recharge.getDate().equals(today.toString()))
                .collect(Collectors.toList());
        if (sellerId != null) {
            filteredRechargeData = filteredRechargeData.stream()
                    .filter(recharge -> Objects.equals(recharge.getSellerId(), sellerId))
                    .collect(Collectors.toList());

        }
        List<tpe> filteredTpeData = tpeData.stream()
                .filter(tpe -> {
                    String tpeDate = tpe.getDate();
                    return tpeDate != null && !tpeDate.equals(today.toString());
                })
                .collect(Collectors.toList());
        if (sellerId != null) {
            filteredTpeData = filteredTpeData.stream()
                    .filter(tpe -> Objects.equals(tpe.getSellerId(), sellerId))
                    .collect(Collectors.toList());

        }

        List<Map<String, Object>> reports = new ArrayList<>();
        Map<String, List<sale>> salesByDate = groupSalesByDate(filteredSalesData, sellerId);
        Map<String, List<recharge>> rechargeByDate = groupRechargeByDate(filteredRechargeData, sellerId);
        Map<String, List<tpe>> tpeByDate = groupTpeByDate(filteredTpeData, sellerId);
        List<recharge> rechargeForDate = new ArrayList<>();
        List<tpe> tpeForDate = new ArrayList<>();
        List<sale> salesForDate = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        for (Map.Entry<String, List<recharge>> rechargeEntry : rechargeByDate.entrySet()) {
            rechargeForDate = rechargeEntry.getValue();
        }
        for (Map.Entry<String, List<tpe>> tpeEntry : tpeByDate.entrySet()) {
            tpeForDate = tpeEntry.getValue();
        }
        for (Map.Entry<String, List<sale>> saleEntry : salesByDate.entrySet()) {

            String date = saleEntry.getKey();
            dates.add(date);
            salesForDate = saleEntry.getValue();

        }
        for (String date : dates) {
            if ((!salesForDate.isEmpty() && !rechargeForDate.isEmpty() && !tpeForDate.isEmpty()) &&
                    (salesForDate.get(0).getDate().equals(tpeForDate.get(0).getDate()) ||
                            salesForDate.get(0).getDate().equals(rechargeForDate.get(0).getDate()))) {
                byte[] reportBytes = generateSalesReport(salesForDate, rechargeForDate, tpeForDate, sellerId);
                String fileName = "Etat_de_Vente " + date + ".docx";

                Map<String, Object> reportInfo = new HashMap<>();
                reportInfo.put("fileName", fileName);
                reportInfo.put("fileBytes", reportBytes);

                reports.add(reportInfo);
            } else {
                byte[] reportBytes = generateSalesReport(salesForDate, new ArrayList<>(), new ArrayList<>(), sellerId);
                String fileName = "Etat_de_Vente " + date + ".docx";

                Map<String, Object> reportInfo = new HashMap<>();
                reportInfo.put("fileName", fileName);
                reportInfo.put("fileBytes", reportBytes);

                reports.add(reportInfo);
            }

        }
        return ResponseEntity.ok(reports);

    }

    private Map<String, List<sale>> groupSalesByDate(List<sale> salesData, Long sellerId) {
        Map<String, List<sale>> salesByDate = new LinkedHashMap<>();

        for (sale sale : salesData) {
            Long sid = (sale.getSellerId() != null) ? sale.getSellerId() : 0;
            if (sid == sellerId) {
                String date = sale.getDate();
                if (!salesByDate.containsKey(date)) {
                    salesByDate.put(date, new ArrayList<>());
                }
                salesByDate.get(date).add(sale);
            }
        }

        return salesByDate;
    }

    private Map<String, List<recharge>> groupRechargeByDate(List<recharge> rechargeData, Long sellerId) {
        Map<String, List<recharge>> rechargeByDate = new LinkedHashMap<>();

        for (recharge recharge : rechargeData) {
            Long sid = (recharge.getSellerId() != null) ? recharge.getSellerId() : 0;
            if (sid == sellerId && recharge.getDate() != null) {
                String date = recharge.getDate();
                if (!rechargeByDate.containsKey(date)) {
                    rechargeByDate.put(date, new ArrayList<>());
                }
                rechargeByDate.get(date).add(recharge);
            }
        }
        return rechargeByDate;
    }

    private Map<String, List<tpe>> groupTpeByDate(List<tpe> tpeData, Long sellerId) {
        Map<String, List<tpe>> tpeByDate = new LinkedHashMap<>();

        for (tpe tpe : tpeData) {
            Long sid = (tpe.getSellerId() != null) ? tpe.getSellerId() : 0;
            if (sid == sellerId && tpe.getDate() != null) {
                String date = tpe.getDate();
                if (!tpeByDate.containsKey(date)) {
                    tpeByDate.put(date, new ArrayList<>());
                }
                tpeByDate.get(date).add(tpe);
            }
        }
        return tpeByDate;
    }

    private byte[] generateSalesReport(List<sale> salesData, List<recharge> rechargeData, List<tpe> tpeData,
            Long sellerId)
            throws IOException, java.io.IOException {
        XWPFDocument document = new XWPFDocument();

        Map<String, List<sale>> salesByDate = groupSalesByDate(salesData, sellerId);
        Map<String, List<recharge>> rechargeByDate = groupRechargeByDate(rechargeData, sellerId);
        Map<String, List<tpe>> tpeByDate = groupTpeByDate(tpeData, sellerId);
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

            spacingParagraph.setSpacingAfter(60);

            XWPFTable table = document.createTable();
            XWPFTableRow headerRow = table.getRow(0);
            headerRow.addNewTableCell().setText("Produit");
            headerRow.addNewTableCell().setText("Quantité");
            headerRow.addNewTableCell().setText("Prix");
            headerRow.addNewTableCell().setText("Num_téléphone");
            headerRow.addNewTableCell().setText("IMEI");
            headerRow.addNewTableCell().setText("Avec carte");

            float totalPrix = 0;
            for (sale sale : salesForDate) {

                XWPFTableRow dataRow = table.createRow();
                String type = (sale.getType() != null) ? String.valueOf(sale.getType()) : "";
                dataRow.getCell(1).setText(type);
                dataRow.getCell(2).setText(String.valueOf(sale.getQuantite()));
                dataRow.getCell(3).setText(String.valueOf(sale.getPrix()));
                dataRow.getCell(4).setText(String.valueOf(sale.getTelephone()));
                dataRow.getCell(5).setText(String.valueOf(sale.getImei()));
                dataRow.getCell(6).setText(String.valueOf(sale.getCard()));
                if (sale.getPrix() != null) {
                    totalPrix = totalPrix + (sale.getQuantite() * sale.getPrix());
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

            int[] columnWidths = { 0, 2000, 1500, 1700, 2400, 2700, 2000 };

            for (int colIndex = 0; colIndex < columnWidths.length; colIndex++) {
                int width = columnWidths[colIndex];

                for (XWPFTableRow row : table.getRows()) {
                    if (row.getTableCells().size() > colIndex) {
                        XWPFTableCell cell = row.getCell(colIndex);

                        if (cell != null) {
                            CTTcPr tcPr = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr()
                                    : cell.getCTTc().addNewTcPr();
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
        for (Map.Entry<String, List<recharge>> entry : rechargeByDate.entrySet()) {

            XWPFParagraph nomParagraph1 = document.createParagraph();
            XWPFRun nomRun1 = nomParagraph1.createRun();
            nomRun1.setText("Opération de Recharge");
            nomRun1.setBold(true);
            List<recharge> rechargeForDate = entry.getValue();
            XWPFParagraph spacingParagraph = document.createParagraph();
            spacingParagraph.setSpacingAfter(9);
            XWPFTable table1 = document.createTable();
            XWPFTableRow headerRow = table1.getRow(0);
            headerRow.addNewTableCell().setText("Solde de départ");
            headerRow.addNewTableCell().setText("Solde dealer");
            headerRow.addNewTableCell().setText("Solde transfert");
            headerRow.addNewTableCell().setText("Reste");
            headerRow.addNewTableCell().setText("Résultat");

            for (recharge recharge : rechargeForDate) {

                XWPFTableRow dataRow = table1.createRow();
                dataRow.getCell(1).setText(String.valueOf(recharge.getDepart()));
                dataRow.getCell(2).setText(String.valueOf(recharge.getDealer()));
                dataRow.getCell(3).setText(String.valueOf(recharge.getTransfert()));
                dataRow.getCell(4).setText(String.valueOf(recharge.getReste()));
                dataRow.getCell(5).setText(String.valueOf(recharge.getResult()));

            }

            int[] columnWidths = { 0, 2000, 1500, 1700, 2400, 2700, 2000 };

            for (int colIndex = 0; colIndex < columnWidths.length; colIndex++) {
                int width = columnWidths[colIndex];

                for (XWPFTableRow row : table1.getRows()) {
                    if (row.getTableCells().size() > colIndex) {
                        XWPFTableCell cell = row.getCell(colIndex);

                        if (cell != null) {
                            CTTcPr tcPr = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr()
                                    : cell.getCTTc().addNewTcPr();
                            CTTblWidth cellWidth = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
                            cellWidth.setType(STTblWidth.DXA);
                            cellWidth.setW(BigInteger.valueOf(width));

                        }
                    }
                }
            }

            for (XWPFTableRow row : table1.getRows()) {
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
        for (Map.Entry<String, List<tpe>> entry : tpeByDate.entrySet()) {
            XWPFParagraph nomParagraph1 = document.createParagraph();
            XWPFRun nomRun1 = nomParagraph1.createRun();
            nomRun1.setText("Opération de TPE");
            nomRun1.setBold(true);
            List<tpe> tpeForDate = entry.getValue();

            XWPFParagraph spacingParagraph = document.createParagraph();

            spacingParagraph.setSpacingAfter(9);
            XWPFTable table1 = document.createTable();
            XWPFTableRow headerRow = table1.getRow(0);
            headerRow.addNewTableCell().setText("Solde de départ");
            headerRow.addNewTableCell().setText("Solde dealer");
            headerRow.addNewTableCell().setText("Solde transfert");
            headerRow.addNewTableCell().setText("Reste");
            headerRow.addNewTableCell().setText("Solde Activation");
            headerRow.addNewTableCell().setText("Résultat");

            for (tpe tpe : tpeForDate) {

                XWPFTableRow dataRow = table1.createRow();
                dataRow.getCell(1).setText(String.valueOf(tpe.getDepart()));
                dataRow.getCell(2).setText(String.valueOf(tpe.getDealer()));
                dataRow.getCell(3).setText(String.valueOf(tpe.getTransfert()));
                dataRow.getCell(4).setText(String.valueOf(tpe.getReste()));
                dataRow.getCell(5).setText(String.valueOf(tpe.getActivation()));
                dataRow.getCell(6).setText(String.valueOf(tpe.getResult()));

            }

            int[] columnWidths = { 0, 2000, 1500, 1700, 2400, 2700, 2000 };

            for (int colIndex = 0; colIndex < columnWidths.length; colIndex++) {
                int width = columnWidths[colIndex];

                for (XWPFTableRow row : table1.getRows()) {
                    if (row.getTableCells().size() > colIndex) {
                        XWPFTableCell cell = row.getCell(colIndex);

                        if (cell != null) {
                            CTTcPr tcPr = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr()
                                    : cell.getCTTc().addNewTcPr();
                            CTTblWidth cellWidth = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
                            cellWidth.setType(STTblWidth.DXA);
                            cellWidth.setW(BigInteger.valueOf(width));

                        }
                    }
                }
            }

            for (XWPFTableRow row : table1.getRows()) {
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
    public ResponseEntity<List<Map<String, Object>>> generateSalesReport1(@RequestParam(required = false) Long sellerId)
            throws IOException, java.io.IOException, ParseException {
        user seller = userService.getSellerInfo(sellerId);
        List<sale> salesData = salesService.getSalesDataForSellerId(sellerId);
        Map<String, Map<Long, List<sale>>> groupedSales = new LinkedHashMap<>();
        for (sale sale : salesData) {
            String date = sale.getDate();
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            String currentYearMonth = dateFormat.format(currentDate);
            Date saleDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            String formattedDate = dateFormat.format(saleDate);
            if (formattedDate.equals(currentYearMonth)) {
                String sellerName = seller.getNom_complet();
                Long sellerid = seller.getId();
                if (!groupedSales.containsKey(date)) {
                    groupedSales.put(date, new LinkedHashMap<>());
                }
                if (!groupedSales.get(date).containsKey(sellerid)) {
                    groupedSales.get(date).put(sellerid, new ArrayList<>());

                    groupedSales.get(date).get(sellerid).add(sale);
                }
            }
        }
        List<recharge> rechargeData = rechargeService.getSalesDataForSellerId(sellerId);
        Map<String, Map<Long, List<recharge>>> groupedRecharge = new LinkedHashMap<>();
        for (recharge recharge : rechargeData) {
            String date = recharge.getDate();
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            String currentYearMonth = dateFormat.format(currentDate);
            Date rechargeDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            String formattedDate = dateFormat.format(rechargeDate);
            if (formattedDate.equals(currentYearMonth)) {
                Long sellerid = seller.getId();
                if (!groupedRecharge.containsKey(date)) {
                    groupedRecharge.put(date, new LinkedHashMap<>());
                }
                if (!groupedRecharge.get(date).containsKey(sellerid)) {
                    groupedRecharge.get(date).put(sellerid, new ArrayList<>());

                    groupedRecharge.get(date).get(sellerid).add(recharge);
                }
            }
        }
        List<tpe> tpeData = tpeService.getSalesDataForSellerId(sellerId);
        Map<String, Map<Long, List<tpe>>> groupedTpe = new LinkedHashMap<>();
        for (tpe tpe : tpeData) {
            String date = tpe.getDate();
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            String currentYearMonth = dateFormat.format(currentDate);
            Date tpeDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            String formattedDate = dateFormat.format(tpeDate);
            if (formattedDate.equals(currentYearMonth)) {
                Long sellerid = seller.getId();
                if (!groupedTpe.containsKey(date)) {
                    groupedTpe.put(date, new LinkedHashMap<>());
                }
                if (!groupedTpe.get(date).containsKey(sellerid)) {
                    groupedTpe.get(date).put(sellerid, new ArrayList<>());

                    groupedTpe.get(date).get(sellerid).add(tpe);
                }
            }
        }
        List<recharge> rechargeForSeller = new ArrayList<>();
        List<tpe> tpeForSeller = new ArrayList<>();
        List<sale> salesForSeller = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        List<Map<String, Object>> reports = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Map.Entry<String, Map<Long, List<sale>>> dateEntry : groupedSales.entrySet()) {
            if (!dateEntry.getKey().equals(today.toString())) {
                dates.add(dateEntry.getKey());
            }
            String date = dateEntry.getKey();
            Map<Long, List<sale>> salesBySeller = dateEntry.getValue();
            for (Map.Entry<Long, List<sale>> sellerEntry : salesBySeller.entrySet()) {
                salesForSeller.addAll(sellerEntry.getValue());
            }
        }
        for (Map.Entry<String, Map<Long, List<recharge>>> dateEntry : groupedRecharge.entrySet()) {
            if (!dates.contains(dateEntry.getKey()) && !dateEntry.getKey().equals(today.toString())) {
                dates.add(dateEntry.getKey());
            }
            Map<Long, List<recharge>> rechargeBySeller = dateEntry.getValue();
            for (Map.Entry<Long, List<recharge>> sellerEntry : rechargeBySeller.entrySet()) {
                rechargeForSeller.addAll(sellerEntry.getValue());
            }
        }
        for (Map.Entry<String, Map<Long, List<tpe>>> dateEntry : groupedTpe.entrySet()) {
            if (!dates.contains(dateEntry.getKey()) && !dateEntry.getKey().equals(today.toString())) {
                dates.add(dateEntry.getKey());
            }
            Map<Long, List<tpe>> tpeBySeller = dateEntry.getValue();
            for (Map.Entry<Long, List<tpe>> sellerEntry : tpeBySeller.entrySet()) {
                tpeForSeller.addAll(sellerEntry.getValue());
            }
        }
        for (String date : dates) {
            byte[] reportBytes = generateSalesReportPDF(date, seller, salesForSeller, rechargeForSeller,
                    tpeForSeller);

            String fileName = "Etat_de_vente_" + seller.getNom_complet() + "_" + date + ".pdf";

            Map<String, Object> reportInfo = new HashMap<>();
            reportInfo.put("fileName", fileName);
            reportInfo.put("fileBytes", reportBytes);

            reports.add(reportInfo);
        }

        return ResponseEntity.ok(reports);
    }

    private byte[] generateSalesReportPDF(String date, user seller, List<sale> salesData, List<recharge> rechargeData,
            List<tpe> tpeData)
            throws IOException, java.io.IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        float margin = 50;
        float margin1 = 10;

        float yStart = page.getMediaBox().getHeight() - margin;
        float yStart1 = yStart - 60;
        float yStart2 = yStart1 - 15;

        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float yPosition = yStart2 - 100;
        int rowsPerPage = 10;

        float tableHeight = 20 * rowsPerPage;
        float bottomMargin = -30;

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
        contentStream.showText("Nom du vendeur : ");
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
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.newLineAtOffset(margin, yStart2 - 14);
        contentStream.showText("Code agence: ");
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.showText(seller.getAgence());
        contentStream.endText();

        float tableYPosition = yPosition;

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
            contentStream.fillRect(margin1, tableYPosition - 16, tableWidth + 72, 15);
            isGrayRow = !isGrayRow;

            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.newLineAtOffset(margin1, tableYPosition - 12);
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
            if (sale.getPrix() != null && sale.getQuantite() != null) {
                totalPrice = totalPrice + (sale.getQuantite() * sale.getPrix());
            }

            contentStream.setNonStrokingColor(Color.BLACK);

        }
        tableYPosition -= 70;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, tableYPosition + 18);
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.showText("Total en DH: " + totalPrice);
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(margin, tableYPosition - 30);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.showText("Soldes de recharge");
        contentStream.endText();
        float textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("Soldes de recharge") / 1000 * 12;
        contentStream.moveTo(margin, tableYPosition - 33);
        contentStream.lineTo(margin + textWidth, tableYPosition - 33);
        contentStream.setStrokingColor(Color.BLACK);
        contentStream.setLineWidth(1f);
        contentStream.stroke();

        tableYPosition -= 73;

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.newLineAtOffset(margin1, tableYPosition);
        contentStream.showText("Solde de départ");
        contentStream.newLineAtOffset(tableWidth * 0.26f, 0);
        contentStream.showText("Solde dealer");
        contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
        contentStream.showText("Solde transfert");
        contentStream.newLineAtOffset(tableWidth * 0.21f, 0);
        contentStream.showText("Reste");
        contentStream.newLineAtOffset(tableWidth * 0.23f, 0);
        contentStream.showText("Résultat");
        contentStream.endText();
        for (recharge recharge : rechargeData) {

            tableYPosition -= 15;
            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.newLineAtOffset(margin1, tableYPosition);
            contentStream.showText(String.valueOf(recharge.getDepart()));
            contentStream.newLineAtOffset(tableWidth * 0.26f, 0);
            contentStream.showText(String.valueOf(recharge.getDealer()));
            contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
            contentStream.showText(String.valueOf(recharge.getTransfert()));
            contentStream.newLineAtOffset(tableWidth * 0.21f, 0);
            contentStream.showText(String.valueOf(recharge.getReste()));
            contentStream.newLineAtOffset(tableWidth * 0.23f, 0);
            contentStream.showText(String.valueOf(recharge.getResult()));
            contentStream.endText();
            contentStream.setNonStrokingColor(Color.BLACK);

        }
        tableYPosition -= 80;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, tableYPosition + 30);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.showText("Soldes de tpe");
        contentStream.endText();
        float textWidth1 = PDType1Font.HELVETICA_BOLD.getStringWidth("Soldes de tpe") / 1000 * 12;
        contentStream.moveTo(margin, tableYPosition + 27);
        contentStream.lineTo(margin + textWidth1, tableYPosition + 27);
        contentStream.setStrokingColor(Color.BLACK);
        contentStream.setLineWidth(1f);
        contentStream.stroke();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.newLineAtOffset(margin1, tableYPosition);
        contentStream.showText("Solde de départ");
        contentStream.newLineAtOffset(tableWidth * 0.26f, 0);
        contentStream.showText("Solde dealer");
        contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
        contentStream.showText("Solde transfert");
        contentStream.newLineAtOffset(tableWidth * 0.21f, 0);
        contentStream.showText("Reste");
        contentStream.newLineAtOffset(tableWidth * 0.20f, 0);
        contentStream.showText("Solde Activation");
        contentStream.newLineAtOffset(tableWidth * 0.21f, 0);
        contentStream.showText("Résultat");
        contentStream.endText();
        for (tpe tpe : tpeData) {

            tableYPosition -= 15;

            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.newLineAtOffset(margin1, tableYPosition);
            contentStream.showText(String.valueOf(tpe.getDepart()));
            contentStream.newLineAtOffset(tableWidth * 0.26f, 0);
            contentStream.showText(String.valueOf(tpe.getDealer()));
            contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
            contentStream.showText(String.valueOf(tpe.getTransfert()));
            contentStream.newLineAtOffset(tableWidth * 0.21f, 0);
            contentStream.showText(String.valueOf(tpe.getReste()));
            contentStream.newLineAtOffset(tableWidth * 0.20f, 0);
            contentStream.showText(String.valueOf(tpe.getActivation()));
            contentStream.newLineAtOffset(tableWidth * 0.21f, 0);
            contentStream.showText(String.valueOf(tpe.getResult()));
            contentStream.endText();
            contentStream.setNonStrokingColor(Color.BLACK);

        }
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
