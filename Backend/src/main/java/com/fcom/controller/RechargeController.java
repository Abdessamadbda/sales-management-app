package com.fcom.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fcom.model.recharge;
import com.fcom.model.sale;
import com.fcom.model.tpe;
import com.fcom.service.RechargeService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Configuration
public class RechargeController {
    @Autowired
    private final RechargeService rechargeService;

    @Autowired
    public RechargeController(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @PostMapping("/seller/recharge")
    public ResponseEntity<recharge> saveSale(@RequestBody recharge recharge) {
        System.out.println(recharge);
        recharge savedRecharge = rechargeService.saveRecharge(recharge);
        if (savedRecharge != null) {
            return ResponseEntity.ok(savedRecharge);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/recharge/{sellerId}")
    public ResponseEntity<List<recharge>> getTpeData(@PathVariable Long sellerId) {
        List<recharge> recharge = rechargeService.getSalesDataForSellerId(sellerId);
        return ResponseEntity.ok(recharge);
    }

    @GetMapping("/aujour/{sellerId}")
    public ResponseEntity<List<recharge>> getRechargeForToday(@PathVariable Long sellerId) {
        LocalDate currentDate = LocalDate.now();
        List<recharge> recharge = rechargeService.getRechargeForDateAndSellerId(currentDate.toString(), sellerId);
        return ResponseEntity.ok(recharge);
    }

    @PostMapping("/seller/recharge/update")
    public ResponseEntity<recharge> updateRecharge(@RequestBody recharge recharge) {
        recharge updatedRecharge = rechargeService.updateRecharge(recharge);
        if (updatedRecharge != null) {
            return ResponseEntity.ok(updatedRecharge);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
