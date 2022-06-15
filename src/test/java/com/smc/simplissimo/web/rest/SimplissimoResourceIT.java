package com.smc.simplissimo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.smc.simplissimo.IntegrationTest;
import com.smc.simplissimo.domain.Simplissimo;
import com.smc.simplissimo.repository.SimplissimoRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SimplissimoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SimplissimoResourceIT {

    private static final String DEFAULT_NOM_APPLICATION = "AAAAAAAAAA";
    private static final String UPDATED_NOM_APPLICATION = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEMANDE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEMANDE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_RETOUR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RETOUR = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/simplissimos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SimplissimoRepository simplissimoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSimplissimoMockMvc;

    private Simplissimo simplissimo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Simplissimo createEntity(EntityManager em) {
        Simplissimo simplissimo = new Simplissimo()
            .nom_application(DEFAULT_NOM_APPLICATION)
            .action(DEFAULT_ACTION)
            .password(DEFAULT_PASSWORD)
            .status(DEFAULT_STATUS)
            .message(DEFAULT_MESSAGE)
            .date_demande(DEFAULT_DATE_DEMANDE)
            .date_retour(DEFAULT_DATE_RETOUR)
            .userId(DEFAULT_USER_ID);
        return simplissimo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Simplissimo createUpdatedEntity(EntityManager em) {
        Simplissimo simplissimo = new Simplissimo()
            .nom_application(UPDATED_NOM_APPLICATION)
            .action(UPDATED_ACTION)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .date_demande(UPDATED_DATE_DEMANDE)
            .date_retour(UPDATED_DATE_RETOUR)
            .userId(UPDATED_USER_ID);
        return simplissimo;
    }

    @BeforeEach
    public void initTest() {
        simplissimo = createEntity(em);
    }

    @Test
    @Transactional
    void createSimplissimo() throws Exception {
        int databaseSizeBeforeCreate = simplissimoRepository.findAll().size();
        // Create the Simplissimo
        restSimplissimoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simplissimo))
            )
            .andExpect(status().isCreated());

        // Validate the Simplissimo in the database
        List<Simplissimo> simplissimoList = simplissimoRepository.findAll();
        assertThat(simplissimoList).hasSize(databaseSizeBeforeCreate + 1);
        Simplissimo testSimplissimo = simplissimoList.get(simplissimoList.size() - 1);
        assertThat(testSimplissimo.getNom_application()).isEqualTo(DEFAULT_NOM_APPLICATION);
        assertThat(testSimplissimo.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testSimplissimo.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testSimplissimo.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSimplissimo.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testSimplissimo.getDate_demande()).isEqualTo(DEFAULT_DATE_DEMANDE);
        assertThat(testSimplissimo.getDate_retour()).isEqualTo(DEFAULT_DATE_RETOUR);
        assertThat(testSimplissimo.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createSimplissimoWithExistingId() throws Exception {
        // Create the Simplissimo with an existing ID
        simplissimo.setId(1L);

        int databaseSizeBeforeCreate = simplissimoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSimplissimoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simplissimo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Simplissimo in the database
        List<Simplissimo> simplissimoList = simplissimoRepository.findAll();
        assertThat(simplissimoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSimplissimos() throws Exception {
        // Initialize the database
        simplissimoRepository.saveAndFlush(simplissimo);

        // Get all the simplissimoList
        restSimplissimoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(simplissimo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom_application").value(hasItem(DEFAULT_NOM_APPLICATION)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].date_demande").value(hasItem(DEFAULT_DATE_DEMANDE.toString())))
            .andExpect(jsonPath("$.[*].date_retour").value(hasItem(DEFAULT_DATE_RETOUR.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    void getSimplissimo() throws Exception {
        // Initialize the database
        simplissimoRepository.saveAndFlush(simplissimo);

        // Get the simplissimo
        restSimplissimoMockMvc
            .perform(get(ENTITY_API_URL_ID, simplissimo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(simplissimo.getId().intValue()))
            .andExpect(jsonPath("$.nom_application").value(DEFAULT_NOM_APPLICATION))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.date_demande").value(DEFAULT_DATE_DEMANDE.toString()))
            .andExpect(jsonPath("$.date_retour").value(DEFAULT_DATE_RETOUR.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID));
    }

    @Test
    @Transactional
    void getNonExistingSimplissimo() throws Exception {
        // Get the simplissimo
        restSimplissimoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSimplissimo() throws Exception {
        // Initialize the database
        simplissimoRepository.saveAndFlush(simplissimo);

        int databaseSizeBeforeUpdate = simplissimoRepository.findAll().size();

        // Update the simplissimo
        Simplissimo updatedSimplissimo = simplissimoRepository.findById(simplissimo.getId()).get();
        // Disconnect from session so that the updates on updatedSimplissimo are not directly saved in db
        em.detach(updatedSimplissimo);
        updatedSimplissimo
            .nom_application(UPDATED_NOM_APPLICATION)
            .action(UPDATED_ACTION)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .date_demande(UPDATED_DATE_DEMANDE)
            .date_retour(UPDATED_DATE_RETOUR)
            .userId(UPDATED_USER_ID);

        restSimplissimoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSimplissimo.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSimplissimo))
            )
            .andExpect(status().isOk());

        // Validate the Simplissimo in the database
        List<Simplissimo> simplissimoList = simplissimoRepository.findAll();
        assertThat(simplissimoList).hasSize(databaseSizeBeforeUpdate);
        Simplissimo testSimplissimo = simplissimoList.get(simplissimoList.size() - 1);
        assertThat(testSimplissimo.getNom_application()).isEqualTo(UPDATED_NOM_APPLICATION);
        assertThat(testSimplissimo.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testSimplissimo.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testSimplissimo.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSimplissimo.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testSimplissimo.getDate_demande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testSimplissimo.getDate_retour()).isEqualTo(UPDATED_DATE_RETOUR);
        assertThat(testSimplissimo.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingSimplissimo() throws Exception {
        int databaseSizeBeforeUpdate = simplissimoRepository.findAll().size();
        simplissimo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimplissimoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, simplissimo.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simplissimo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Simplissimo in the database
        List<Simplissimo> simplissimoList = simplissimoRepository.findAll();
        assertThat(simplissimoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSimplissimo() throws Exception {
        int databaseSizeBeforeUpdate = simplissimoRepository.findAll().size();
        simplissimo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimplissimoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simplissimo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Simplissimo in the database
        List<Simplissimo> simplissimoList = simplissimoRepository.findAll();
        assertThat(simplissimoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSimplissimo() throws Exception {
        int databaseSizeBeforeUpdate = simplissimoRepository.findAll().size();
        simplissimo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimplissimoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simplissimo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Simplissimo in the database
        List<Simplissimo> simplissimoList = simplissimoRepository.findAll();
        assertThat(simplissimoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSimplissimoWithPatch() throws Exception {
        // Initialize the database
        simplissimoRepository.saveAndFlush(simplissimo);

        int databaseSizeBeforeUpdate = simplissimoRepository.findAll().size();

        // Update the simplissimo using partial update
        Simplissimo partialUpdatedSimplissimo = new Simplissimo();
        partialUpdatedSimplissimo.setId(simplissimo.getId());

        partialUpdatedSimplissimo
            .action(UPDATED_ACTION)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS)
            .date_retour(UPDATED_DATE_RETOUR)
            .userId(UPDATED_USER_ID);

        restSimplissimoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimplissimo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSimplissimo))
            )
            .andExpect(status().isOk());

        // Validate the Simplissimo in the database
        List<Simplissimo> simplissimoList = simplissimoRepository.findAll();
        assertThat(simplissimoList).hasSize(databaseSizeBeforeUpdate);
        Simplissimo testSimplissimo = simplissimoList.get(simplissimoList.size() - 1);
        assertThat(testSimplissimo.getNom_application()).isEqualTo(DEFAULT_NOM_APPLICATION);
        assertThat(testSimplissimo.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testSimplissimo.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testSimplissimo.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSimplissimo.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testSimplissimo.getDate_demande()).isEqualTo(DEFAULT_DATE_DEMANDE);
        assertThat(testSimplissimo.getDate_retour()).isEqualTo(UPDATED_DATE_RETOUR);
        assertThat(testSimplissimo.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateSimplissimoWithPatch() throws Exception {
        // Initialize the database
        simplissimoRepository.saveAndFlush(simplissimo);

        int databaseSizeBeforeUpdate = simplissimoRepository.findAll().size();

        // Update the simplissimo using partial update
        Simplissimo partialUpdatedSimplissimo = new Simplissimo();
        partialUpdatedSimplissimo.setId(simplissimo.getId());

        partialUpdatedSimplissimo
            .nom_application(UPDATED_NOM_APPLICATION)
            .action(UPDATED_ACTION)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .date_demande(UPDATED_DATE_DEMANDE)
            .date_retour(UPDATED_DATE_RETOUR)
            .userId(UPDATED_USER_ID);

        restSimplissimoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimplissimo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSimplissimo))
            )
            .andExpect(status().isOk());

        // Validate the Simplissimo in the database
        List<Simplissimo> simplissimoList = simplissimoRepository.findAll();
        assertThat(simplissimoList).hasSize(databaseSizeBeforeUpdate);
        Simplissimo testSimplissimo = simplissimoList.get(simplissimoList.size() - 1);
        assertThat(testSimplissimo.getNom_application()).isEqualTo(UPDATED_NOM_APPLICATION);
        assertThat(testSimplissimo.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testSimplissimo.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testSimplissimo.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSimplissimo.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testSimplissimo.getDate_demande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testSimplissimo.getDate_retour()).isEqualTo(UPDATED_DATE_RETOUR);
        assertThat(testSimplissimo.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSimplissimo() throws Exception {
        int databaseSizeBeforeUpdate = simplissimoRepository.findAll().size();
        simplissimo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimplissimoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, simplissimo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(simplissimo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Simplissimo in the database
        List<Simplissimo> simplissimoList = simplissimoRepository.findAll();
        assertThat(simplissimoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSimplissimo() throws Exception {
        int databaseSizeBeforeUpdate = simplissimoRepository.findAll().size();
        simplissimo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimplissimoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(simplissimo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Simplissimo in the database
        List<Simplissimo> simplissimoList = simplissimoRepository.findAll();
        assertThat(simplissimoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSimplissimo() throws Exception {
        int databaseSizeBeforeUpdate = simplissimoRepository.findAll().size();
        simplissimo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimplissimoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(simplissimo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Simplissimo in the database
        List<Simplissimo> simplissimoList = simplissimoRepository.findAll();
        assertThat(simplissimoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSimplissimo() throws Exception {
        // Initialize the database
        simplissimoRepository.saveAndFlush(simplissimo);

        int databaseSizeBeforeDelete = simplissimoRepository.findAll().size();

        // Delete the simplissimo
        restSimplissimoMockMvc
            .perform(delete(ENTITY_API_URL_ID, simplissimo.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Simplissimo> simplissimoList = simplissimoRepository.findAll();
        assertThat(simplissimoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
