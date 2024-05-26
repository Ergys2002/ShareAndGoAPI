package com.app.ShareAndGo.entities;

import com.app.ShareAndGo.enums.Role;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Admin extends BaseEntity{
    private String username;
    private String email;
    private String password;
    private String NID;
    private Role role;
    private boolean disabled;
    private int salary;

}
