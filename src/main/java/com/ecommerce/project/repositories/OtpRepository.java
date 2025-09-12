package com.ecommerce.project.repositories;

import com.ecommerce.project.model.OtpEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {

    Optional<OtpEntity> findByUserUserId(Long userId);


    Optional<OtpEntity> findTopByEmailOrderByExpiryDateDesc(String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM OtpEntity o WHERE o.user.email = :email")
    void deleteByEmail(@Param("email") String email);

}
