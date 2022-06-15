package com.smc.simplissimo.repository;

import com.smc.simplissimo.domain.Simplissimo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Simplissimo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SimplissimoRepository extends JpaRepository<Simplissimo, Long> {}
