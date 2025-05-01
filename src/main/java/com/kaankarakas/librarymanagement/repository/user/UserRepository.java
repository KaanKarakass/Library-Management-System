package com.kaankarakas.librarymanagement.repository.user;

import com.kaankarakas.librarymanagement.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String userName);
}
