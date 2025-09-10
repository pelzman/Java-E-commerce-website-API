package com.ecommerce.project.payload;

import com.ecommerce.project.model.AppRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Long roleId;

    private AppRole roleName;
}
