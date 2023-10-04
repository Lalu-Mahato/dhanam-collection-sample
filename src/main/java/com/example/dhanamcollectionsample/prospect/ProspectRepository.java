package com.example.dhanamcollectionsample.prospect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dhanamcollectionsample.prospect.entity.Prospect;

@Repository
public interface ProspectRepository extends JpaRepository<Prospect, Integer> {

}
