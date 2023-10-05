package com.example.dhanamcollectionsample.emi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dhanamcollectionsample.emi.entity.Emi;

@Repository
public interface EmiRepository extends JpaRepository<Emi, Integer> {

}
