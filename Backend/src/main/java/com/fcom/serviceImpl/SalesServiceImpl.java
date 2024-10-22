package com.fcom.serviceImpl;

import com.fcom.dbutil.DBUtil;
import com.fcom.model.recharge;
import com.fcom.model.sale;
import com.fcom.model.tpe;
import com.fcom.model.user;
import com.fcom.repository.RechargeRepository;
import com.fcom.repository.SalesRepository;
import com.fcom.repository.TpeRepository;
import com.fcom.repository.UserRepository;
import com.fcom.service.RechargeService;
import com.fcom.service.SalesService;
import com.fcom.service.TpeService;
import com.fcom.service.UserService;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesServiceImpl implements SalesService {
    @Autowired
    private final RechargeServiceImpl rechargeServiceimpl;
    @Autowired
    private final TpeServiceImpl tpeServiceimpl;

    private final SalesRepository salesRepository;
    private final RechargeRepository rechargeRepository;
    private final TpeRepository tpeRepository;
    Connection connection;

    @Autowired
    public SalesServiceImpl(SalesRepository salesRepository, RechargeRepository rechargeRepository,
            TpeRepository tpeRepository, RechargeServiceImpl rechargeServiceimpl, TpeServiceImpl tpeServiceimpl)
            throws SQLException {
        this.salesRepository = salesRepository;
        this.rechargeRepository = rechargeRepository;
        this.tpeRepository = tpeRepository;
        connection = DBUtil.getConnection();
        this.rechargeServiceimpl = rechargeServiceimpl;
        this.tpeServiceimpl = tpeServiceimpl;
    }

    @Override
    public List<sale> getSalesDataForSellerId(Long id) {
        List<sale> salesData = salesRepository.findBySellerId(id);
        return salesData;
    }

    @Override
    public byte[] generateSalesReportForSeller(user seller) throws IOException {
        List<sale> sales = getSalesForDateAndSellerId(LocalDate.now().toString(), seller.getId());
        List<recharge> rechargeData = rechargeServiceimpl.getRechargeForDateAndSellerId(LocalDate.now().toString(),
                seller.getId());
        List<tpe> tpeData = tpeServiceimpl.getTpeForDateAndSellerId(LocalDate.now().toString(), seller.getId());
        String today = LocalDate.now().toString();
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            PDFont font = PDType1Font.HELVETICA_BOLD;
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
            float yPositionStart = yStart - tableHeight - bottomMargin;

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 15);
            contentStream.setNonStrokingColor(Color.ORANGE);

            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yStart);
            contentStream.showText("Etat de vente du: " + today);
            contentStream.endText();
            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yStart1);
            contentStream.showText("Nom du vendeur : ");
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.showText(seller.getNom_complet());
            contentStream.endText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yStart2);
            contentStream.showText("Ville : ");
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

            float tableYPosition = yPositionStart;
            contentStream.beginText();
            contentStream.setFont(font, 10);
            contentStream.newLineAtOffset(margin1, tableYPosition);
            contentStream.showText("Produit");
            contentStream.newLineAtOffset(tableWidth * 0.30f, 0);
            contentStream.showText("Quantité");
            contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
            contentStream.showText("Prix en DH");
            contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
            contentStream.showText("Num_Téléphone");
            contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
            contentStream.showText("IMEI");
            contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
            contentStream.showText("Avec carte");
            contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
            contentStream.endText();
            float totalPrice = 0;

            contentStream.setFont(PDType1Font.HELVETICA, 10);
            float Recharge = 0;
            float Tpe = 0;
            boolean isGrayRow = true;

            for (sale sale : sales) {
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
                contentStream.newLineAtOffset(margin1, tableYPosition - 12);
                String type = (sale.getType() != null) ? String.valueOf(sale.getType()) : "";
                contentStream.showText(type);
                contentStream.newLineAtOffset(tableWidth * 0.30f, 0);
                Long quantite = sale.getQuantite();
                String prixText1 = (quantite != null) ? String.valueOf(quantite) : "";
                contentStream.showText(prixText1);
                contentStream.newLineAtOffset(tableWidth * 0.18f, 0);

                Float prix = sale.getPrix();
                String prixText = (prix != null) ? String.valueOf(prix) : "";
                contentStream.showText(prixText);
                contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
                String tele = sale.getTelephone();
                String tele1 = (tele != null) ? tele : "";
                contentStream.showText(tele1);
                contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
                String imei = sale.getImei();
                String imei1 = (imei != null) ? imei : "";
                contentStream.showText(imei1);
                contentStream.newLineAtOffset(tableWidth * 0.18f, 0);
                String card = sale.getCard();
                String card1 = (card != null) ? card : "";
                contentStream.showText(card1);
                contentStream.endText();
                if (prix != null && quantite != null) {
                    totalPrice = totalPrice + (quantite * prix);
                }

            }
            contentStream.setNonStrokingColor(Color.BLACK);
            tableYPosition -= 70;
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, tableYPosition + 18);
            contentStream.setFont(font, 10);
            contentStream.showText("Total en DH:");
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(tableWidth * 0.66f, tableYPosition + 18);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.showText(String.valueOf(totalPrice));
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
            tableYPosition -= 70;
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
            return outputStream.toByteArray();
        }
    }

    @Override
    public List<sale> getSalesForDateAndSellerId(String date, Long id) {
        return salesRepository.findByDateAndSellerId(date, id);
    }

    @Override
    public sale updateSale(sale sale) {
        Optional<sale> existingSale = salesRepository.findById(sale.getId());
        if (existingSale.isPresent()) {
            sale updatedSale = existingSale.get();
            updatedSale.setNom(sale.getNom());
            updatedSale.setType(sale.getType());
            updatedSale.setQuantite(sale.getQuantite());
            updatedSale.setPrix(sale.getPrix());
            updatedSale.setTelephone(sale.getTelephone());
            updatedSale.setImei(sale.getImei());
            updatedSale.setCard(sale.getCard());

            return salesRepository.save(updatedSale);
        } else {
            return null;
        }
    }

    @Override
    public sale saveSale(sale sale) {
        return salesRepository.save(sale);
    }

    @Override
    public sale getSaleById(Long id) {
        Optional<sale> saleOptional = salesRepository.findById(id);
        return saleOptional.orElse(null);
    }

    public List<sale> getSalesForDate(String date) {
        return salesRepository.findByDate(date);
    }

    public void deleteSale(Long saleId) {
        salesRepository.deleteById(saleId);
    }

    public List<sale> getSalesData() {
        return salesRepository.findAll();
    }

}
