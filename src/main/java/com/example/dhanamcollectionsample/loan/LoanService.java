package com.example.dhanamcollectionsample.loan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dhanamcollectionsample.loan.entity.Loan;
import com.example.dhanamcollectionsample.prospect.entity.Prospect;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    public Loan create(Loan loan) {
        return loanRepository.save(loan);
    }

    public Loan mappingToProspect(Loan loan, Prospect prospect) {
        loan.setProspect(prospect);
        return loanRepository.save(loan);
    }

    public ResponseEntity<Object> findAll() {
        List<Loan> loans = loanRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(loans);
    }
}
