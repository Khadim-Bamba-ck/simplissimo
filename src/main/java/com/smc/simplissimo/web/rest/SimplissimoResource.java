package com.smc.simplissimo.web.rest;

import com.smc.simplissimo.domain.Simplissimo;
import com.smc.simplissimo.repository.SimplissimoRepository;
import com.smc.simplissimo.service.SimplissimoService;
import com.smc.simplissimo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.smc.simplissimo.domain.Simplissimo}.
 */
@RestController
@RequestMapping("/api")
public class SimplissimoResource {

    private final Logger log = LoggerFactory.getLogger(SimplissimoResource.class);

    private static final String ENTITY_NAME = "simplissimoSimplissimo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SimplissimoService simplissimoService;

    private final SimplissimoRepository simplissimoRepository;

    public SimplissimoResource(SimplissimoService simplissimoService, SimplissimoRepository simplissimoRepository) {
        this.simplissimoService = simplissimoService;
        this.simplissimoRepository = simplissimoRepository;
    }

    /**
     * {@code POST  /simplissimos} : Create a new simplissimo.
     *
     * @param simplissimo the simplissimo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new simplissimo, or with status {@code 400 (Bad Request)} if the simplissimo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/simplissimos")
    public ResponseEntity<Simplissimo> createSimplissimo(@RequestBody Simplissimo simplissimo) throws URISyntaxException {
        log.debug("REST request to save Simplissimo : {}", simplissimo);
        if (simplissimo.getId() != null) {
            throw new BadRequestAlertException("A new simplissimo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Simplissimo result = simplissimoService.save(simplissimo);
        return ResponseEntity
            .created(new URI("/api/simplissimos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /simplissimos/:id} : Updates an existing simplissimo.
     *
     * @param id the id of the simplissimo to save.
     * @param simplissimo the simplissimo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated simplissimo,
     * or with status {@code 400 (Bad Request)} if the simplissimo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the simplissimo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/simplissimos/{id}")
    public ResponseEntity<Simplissimo> updateSimplissimo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Simplissimo simplissimo
    ) throws URISyntaxException {
        log.debug("REST request to update Simplissimo : {}, {}", id, simplissimo);
        if (simplissimo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, simplissimo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!simplissimoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Simplissimo result = simplissimoService.update(simplissimo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, simplissimo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /simplissimos/:id} : Partial updates given fields of an existing simplissimo, field will ignore if it is null
     *
     * @param id the id of the simplissimo to save.
     * @param simplissimo the simplissimo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated simplissimo,
     * or with status {@code 400 (Bad Request)} if the simplissimo is not valid,
     * or with status {@code 404 (Not Found)} if the simplissimo is not found,
     * or with status {@code 500 (Internal Server Error)} if the simplissimo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/simplissimos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Simplissimo> partialUpdateSimplissimo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Simplissimo simplissimo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Simplissimo partially : {}, {}", id, simplissimo);
        if (simplissimo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, simplissimo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!simplissimoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Simplissimo> result = simplissimoService.partialUpdate(simplissimo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, simplissimo.getId().toString())
        );
    }

    /**
     * {@code GET  /simplissimos} : get all the simplissimos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of simplissimos in body.
     */
    @GetMapping("/simplissimos")
    public ResponseEntity<List<Simplissimo>> getAllSimplissimos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Simplissimos");
        Page<Simplissimo> page = simplissimoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /simplissimos/:id} : get the "id" simplissimo.
     *
     * @param id the id of the simplissimo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the simplissimo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/simplissimos/{id}")
    public ResponseEntity<Simplissimo> getSimplissimo(@PathVariable Long id) {
        log.debug("REST request to get Simplissimo : {}", id);
        Optional<Simplissimo> simplissimo = simplissimoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(simplissimo);
    }

    /**
     * {@code DELETE  /simplissimos/:id} : delete the "id" simplissimo.
     *
     * @param id the id of the simplissimo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/simplissimos/{id}")
    public ResponseEntity<Void> deleteSimplissimo(@PathVariable Long id) {
        log.debug("REST request to delete Simplissimo : {}", id);
        simplissimoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
