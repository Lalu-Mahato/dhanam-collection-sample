package com.example.dhanamcollectionsample.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dhanamcollectionsample.group.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

}
