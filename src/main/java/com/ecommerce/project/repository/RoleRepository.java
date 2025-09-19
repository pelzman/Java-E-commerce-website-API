package com.ecommerce.project.repository;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);

    boolean existsByRoleName(AppRole appRole);

    Optional<Role> findByRoleId(Long roleId);

//    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.users WHERE r.roleName = :roleName")
//    Optional<Role> findByRoleName(@Param("roleName") AppRole roleName);
//
//    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.users WHERE r.id IN :ids")
//    List<Role> findByRoleId(@Param("ids") Set<Long> ids);
}
