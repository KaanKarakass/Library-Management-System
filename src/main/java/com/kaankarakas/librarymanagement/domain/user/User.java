package com.kaankarakas.librarymanagement.domain.user;

import com.kaankarakas.librarymanagement.api.constants.SchemaConstants;
import com.kaankarakas.librarymanagement.domain.base.BaseEntity;
import com.kaankarakas.librarymanagement.enums.BookStatus;
import com.kaankarakas.librarymanagement.enums.UserRole;
import com.kaankarakas.librarymanagement.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static com.kaankarakas.librarymanagement.constants.LibraryManagementDefinitionConstants.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(schema = SchemaConstants.LIBRARY_MANAGEMENT_SCHEMA, uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")})
public class User extends BaseEntity implements UserDetails {

    @Size(max = NAME_MAX_LENGTH)
    @NotNull
    private String name;

    @Size(max = SHORT_NAME_MAX_LENGTH)
    @NotNull
    private String username;

    @Size(max = EMAIL_MAX_LENGTH)
    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserStatus userStatus = UserStatus.getDefaultUserStatus();

    // Implementations for UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role.name());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.userStatus == UserStatus.ACTIVE;
    }
}
