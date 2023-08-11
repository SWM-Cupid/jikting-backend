package com.cupid.jikting.recommend.repository;

import com.cupid.jikting.recommend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

}
