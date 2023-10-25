package com.fcom.service;

import com.fcom.model.recharge;
import com.fcom.model.sale;
import com.fcom.model.tpe;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface TpeService {
    tpe saveTpe(tpe tpe);

    public List<tpe> getTpeData();

    public tpe updateRecharge(tpe tpe);

    public List<tpe> getTpeForDateAndSellerId(String date, Long id);

    public List<tpe> getSalesDataForSellerId(Long id);

}