package com.example.dhanamcollectionsample.emi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dhanamcollectionsample.emi.entity.Emi;
import com.example.dhanamcollectionsample.loan.entity.Loan;

@Service
public class EmiService {
    @Autowired
    private EmiRepository emiRepository;

    public Emi create(Emi emi) {
        return emiRepository.save(emi);
    }

    public ResponseEntity<Object> findAll() {
        List<Emi> emis = emiRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(emis);
    }

    public Emi mappingToLoan(Emi emi, Loan loan) {
        emi.setLoan(loan);
        return emiRepository.save(emi);
    }

}
