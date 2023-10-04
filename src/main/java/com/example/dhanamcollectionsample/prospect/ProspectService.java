package com.example.dhanamcollectionsample.prospect;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dhanamcollectionsample.group.entity.Group;
import com.example.dhanamcollectionsample.prospect.entity.Prospect;

@Service
public class ProspectService {
    @Autowired
    private ProspectRepository prospectRepository;

    public Prospect create(Prospect prospect) {
        return prospectRepository.save(prospect);
    }

    public Prospect mappingToGroup(Group group, Prospect prospect) {
        prospect.setGroup(group);
        return prospectRepository.save(prospect);
    }

    public ResponseEntity<Object> findAll() {
        List<Prospect> prospects = prospectRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(prospects);
    }
}
