package com.fcom.serviceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fcom.dbutil.DBUtil;
import com.fcom.model.recharge;
import com.fcom.model.sale;
import com.fcom.repository.RechargeRepository;
import com.fcom.service.RechargeService;

@Service
public class RechargeServiceImpl implements RechargeService {
    private final RechargeRepository rechargeRepository;

    @Autowired
    public RechargeServiceImpl(RechargeRepository rechargeRepository) throws SQLException {
        this.rechargeRepository = rechargeRepository;
    }

    @Override
    public List<recharge> getRechargeForDateAndSellerId(String date, Long id) {
        return rechargeRepository.findByDateAndSellerId(date, id);
    }

    @Override
    public recharge saveRecharge(recharge recharge) {
        return rechargeRepository.save(recharge);
    }

    @Override
    public List<recharge> getSalesDataForSellerId(Long id) {
        List<recharge> rechargeData = rechargeRepository.findBySellerId(id);
        return rechargeData;
    }

    @Override
    public List<recharge> getRechargeData() {
        return rechargeRepository.findAll();
    }

    @Override
    public recharge updateRecharge(recharge recharge) {
        Optional<recharge> existingRecharge = rechargeRepository.findById(recharge.getId());
        if (existingRecharge.isPresent()) {
            recharge updatedRecharge = existingRecharge.get();
            updatedRecharge.setDepart(recharge.getDepart());
            updatedRecharge.setDealer(recharge.getDealer());
            updatedRecharge.setTransfert(recharge.getTransfert());
            updatedRecharge.setReste(recharge.getReste());
            updatedRecharge.setResult(recharge.getResult());

            return rechargeRepository.save(updatedRecharge);
        } else {
            return null;
        }
    }

}
