package com.kaankarakas.librarymanagement.repository.user;

import com.kaankarakas.librarymanagement.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
