package com.fcom.service;

import com.fcom.model.recharge;
import com.fcom.model.sale;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface RechargeService {
    recharge saveRecharge(recharge recharge);

    public List<recharge> getRechargeData();

    public recharge updateRecharge(recharge recharge);

    public List<recharge> getRechargeForDateAndSellerId(String date, Long id);

    public List<recharge> getSalesDataForSellerId(Long id);

}
