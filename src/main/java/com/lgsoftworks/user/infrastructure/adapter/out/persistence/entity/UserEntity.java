package com.lgsoftworks.user.infrastructure.adapter.out.persistence.entity;

import com.lgsoftworks.shared.infrastructure.persistence.BaseEntity;
import com.lgsoftworks.user.domain.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true)
    private String cellphone;

    @Column(unique = true)
    @Email
    private String email;

    @Length(min = 8, message = "Contraseña debe tener más de 8 caracteres")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "image_url")
    private String imageUrl;

}

