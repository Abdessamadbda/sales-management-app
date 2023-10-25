package com.fcom.serviceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fcom.dbutil.DBUtil;
import com.fcom.model.tpe;
import com.fcom.model.recharge;
import com.fcom.model.sale;
import com.fcom.repository.TpeRepository;
import com.fcom.service.TpeService;

@Service
public class TpeServiceImpl implements TpeService {
    private final TpeRepository tpeRepository;

    @Autowired
    public TpeServiceImpl(TpeRepository tpeRepository) throws SQLException {
        this.tpeRepository = tpeRepository;
    }

    @Override
    public tpe saveTpe(tpe tpe) {
        return tpeRepository.save(tpe);
    }

    @Override
    public List<tpe> getTpeData() {
        return tpeRepository.findAll();
    }

    @Override
    public List<tpe> getSalesDataForSellerId(Long id) {
        List<tpe> tpeData = tpeRepository.findBySellerId(id);
        return tpeData;
    }

    @Override
    public List<tpe> getTpeForDateAndSellerId(String date, Long id) {
        return tpeRepository.findByDateAndSellerId(date, id);
    }

    @Override
    public tpe updateRecharge(tpe tpe) {
        Optional<tpe> existingTpe = tpeRepository.findById(tpe.getId());
        if (existingTpe.isPresent()) {
            tpe updatedTpe = existingTpe.get();
            updatedTpe.setDepart(tpe.getDepart());
            updatedTpe.setDealer(tpe.getDealer());
            updatedTpe.setTransfert(tpe.getTransfert());
            updatedTpe.setReste(tpe.getReste());
            updatedTpe.setActivation(tpe.getActivation());
            updatedTpe.setResult(tpe.getResult());

            return tpeRepository.save(updatedTpe);
        } else {
            return null;
        }
    }

}
