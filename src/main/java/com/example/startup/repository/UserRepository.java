package com.example.startup.repository;

import com.example.startup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByRefreshToken(String token);
    Optional<User> findByKey(String key);
    boolean existsByPhone(String phone);

    @Query("""
    SELECT u FROM User u
    WHERE (:field IS NULL OR u.name = :field OR u.phone = :field)
    AND (:role IS NULL OR u.role = :role)
    AND (:status IS NULL OR u.status = :status)
    """)
    List<User> findUsers(@Param("role") String role,
                         @Param("field") String field,
                         @Param("status") String status);

}
