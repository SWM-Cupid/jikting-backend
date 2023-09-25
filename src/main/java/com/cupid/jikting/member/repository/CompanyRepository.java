package com.cupid.jikting.member.repository;

import com.cupid.jikting.member.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByEmail(String email);
}
