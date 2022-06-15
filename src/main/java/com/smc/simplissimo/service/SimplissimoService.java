package com.smc.simplissimo.service;

import com.smc.simplissimo.domain.Simplissimo;
import com.smc.simplissimo.repository.SimplissimoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Simplissimo}.
 */
@Service
@Transactional
public class SimplissimoService {

    private final Logger log = LoggerFactory.getLogger(SimplissimoService.class);

    private final SimplissimoRepository simplissimoRepository;

    public SimplissimoService(SimplissimoRepository simplissimoRepository) {
        this.simplissimoRepository = simplissimoRepository;
    }

    /**
     * Save a simplissimo.
     *
     * @param simplissimo the entity to save.
     * @return the persisted entity.
     */
    public Simplissimo save(Simplissimo simplissimo) {
        log.debug("Request to save Simplissimo : {}", simplissimo);
        return simplissimoRepository.save(simplissimo);
    }

    /**
     * Update a simplissimo.
     *
     * @param simplissimo the entity to save.
     * @return the persisted entity.
     */
    public Simplissimo update(Simplissimo simplissimo) {
        log.debug("Request to save Simplissimo : {}", simplissimo);
        return simplissimoRepository.save(simplissimo);
    }

    /**
     * Partially update a simplissimo.
     *
     * @param simplissimo the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Simplissimo> partialUpdate(Simplissimo simplissimo) {
        log.debug("Request to partially update Simplissimo : {}", simplissimo);

        return simplissimoRepository
            .findById(simplissimo.getId())
            .map(existingSimplissimo -> {
                if (simplissimo.getNom_application() != null) {
                    existingSimplissimo.setNom_application(simplissimo.getNom_application());
                }
                if (simplissimo.getAction() != null) {
                    existingSimplissimo.setAction(simplissimo.getAction());
                }
                if (simplissimo.getPassword() != null) {
                    existingSimplissimo.setPassword(simplissimo.getPassword());
                }
                if (simplissimo.getStatus() != null) {
                    existingSimplissimo.setStatus(simplissimo.getStatus());
                }
                if (simplissimo.getMessage() != null) {
                    existingSimplissimo.setMessage(simplissimo.getMessage());
                }
                if (simplissimo.getDate_demande() != null) {
                    existingSimplissimo.setDate_demande(simplissimo.getDate_demande());
                }
                if (simplissimo.getDate_retour() != null) {
                    existingSimplissimo.setDate_retour(simplissimo.getDate_retour());
                }
                if (simplissimo.getUserId() != null) {
                    existingSimplissimo.setUserId(simplissimo.getUserId());
                }

                return existingSimplissimo;
            })
            .map(simplissimoRepository::save);
    }

    /**
     * Get all the simplissimos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Simplissimo> findAll(Pageable pageable) {
        log.debug("Request to get all Simplissimos");
        return simplissimoRepository.findAll(pageable);
    }

    /**
     * Get one simplissimo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Simplissimo> findOne(Long id) {
        log.debug("Request to get Simplissimo : {}", id);
        return simplissimoRepository.findById(id);
    }

    /**
     * Delete the simplissimo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Simplissimo : {}", id);
        simplissimoRepository.deleteById(id);
    }
}
