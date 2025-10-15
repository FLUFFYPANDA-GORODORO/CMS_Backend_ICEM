package com.example.ICEM_BACKEND.repository;

import com.example.ICEM_BACKEND.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
}
