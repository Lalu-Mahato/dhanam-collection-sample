package com.example.dhanamcollectionsample.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dhanamcollectionsample.group.entity.Group;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    public Group create(Group group) {
        return groupRepository.save(group);
    }

    public ResponseEntity<Object> findAll() {
        List<Group> groups = groupRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(groups);
    }
}
