package com.ecom.dao;

import com.ecom.enums.ERole;
import com.ecom.modal.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleDao extends JpaRepository<RoleModel, Integer> {
    Optional<RoleModel> findByName(ERole name);
}
