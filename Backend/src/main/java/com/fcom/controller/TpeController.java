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
import com.fcom.model.tpe;
import com.fcom.service.SalesService;
import com.fcom.service.TpeService;
import com.fcom.service.UserService;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Configuration
public class TpeController {
    @Autowired
    private final TpeService tpeService;

    @Autowired
    public TpeController(TpeService tpeService) {
        this.tpeService = tpeService;
    }

    @PostMapping("/seller/tpe")
    public ResponseEntity<tpe> saveSale(@RequestBody tpe tpe) {
        tpe savedTpe = tpeService.saveTpe(tpe);
        if (savedTpe != null) {
            return ResponseEntity.ok(savedTpe);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/aujour1/{sellerId}")
    public ResponseEntity<List<tpe>> getTpeForToday(@PathVariable Long sellerId) {
        LocalDate currentDate = LocalDate.now();
        List<tpe> tpe = tpeService.getTpeForDateAndSellerId(currentDate.toString(), sellerId);
        return ResponseEntity.ok(tpe);
    }

    @GetMapping("/tpe/{sellerId}")
    public ResponseEntity<List<tpe>> getTpeData(@PathVariable Long sellerId) {
        List<tpe> tpe = tpeService.getSalesDataForSellerId(sellerId);
        return ResponseEntity.ok(tpe);
    }

    @PostMapping("/seller/tpe/update")
    public ResponseEntity<tpe> updateRecharge(@RequestBody tpe tpe) {
        tpe updatedTpe = tpeService.updateRecharge(tpe);
        if (updatedTpe != null) {
            return ResponseEntity.ok(updatedTpe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
