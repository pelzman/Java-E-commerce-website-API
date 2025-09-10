package com.ecommerce.project.repositpries;

import com.ecommerce.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository  extends JpaRepository<Role, Long> {
}
