package com.cupid.jikting.member.repository;

import com.cupid.jikting.member.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByEmail(String email);

    Optional<Company> findByEmail(String email);
}
