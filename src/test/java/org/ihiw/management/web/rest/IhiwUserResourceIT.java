package org.ihiw.management.web.rest;

import org.ihiw.management.IhiwManagementApp;
import org.ihiw.management.domain.IhiwUser;
import org.ihiw.management.repository.IhiwUserRepository;
import org.ihiw.management.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.ihiw.management.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link IhiwUserResource} REST controller.
 */
@SpringBootTest(classes = IhiwManagementApp.class)
public class IhiwUserResourceIT {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private IhiwUserRepository ihiwUserRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restIhiwUserMockMvc;

    private IhiwUser ihiwUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IhiwUserResource ihiwUserResource = new IhiwUserResource(ihiwUserRepository);
        this.restIhiwUserMockMvc = MockMvcBuilders.standaloneSetup(ihiwUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IhiwUser createEntity(EntityManager em) {
        IhiwUser ihiwUser = new IhiwUser()
            .phone(DEFAULT_PHONE);
        return ihiwUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IhiwUser createUpdatedEntity(EntityManager em) {
        IhiwUser ihiwUser = new IhiwUser()
            .phone(UPDATED_PHONE);
        return ihiwUser;
    }

    @BeforeEach
    public void initTest() {
        ihiwUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createIhiwUser() throws Exception {
        int databaseSizeBeforeCreate = ihiwUserRepository.findAll().size();

        // Create the IhiwUser
        restIhiwUserMockMvc.perform(post("/api/ihiw-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ihiwUser)))
            .andExpect(status().isCreated());

        // Validate the IhiwUser in the database
        List<IhiwUser> ihiwUserList = ihiwUserRepository.findAll();
        assertThat(ihiwUserList).hasSize(databaseSizeBeforeCreate + 1);
        IhiwUser testIhiwUser = ihiwUserList.get(ihiwUserList.size() - 1);
        assertThat(testIhiwUser.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createIhiwUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ihiwUserRepository.findAll().size();

        // Create the IhiwUser with an existing ID
        ihiwUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIhiwUserMockMvc.perform(post("/api/ihiw-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ihiwUser)))
            .andExpect(status().isBadRequest());

        // Validate the IhiwUser in the database
        List<IhiwUser> ihiwUserList = ihiwUserRepository.findAll();
        assertThat(ihiwUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllIhiwUsers() throws Exception {
        // Initialize the database
        ihiwUserRepository.saveAndFlush(ihiwUser);

        // Get all the ihiwUserList
        restIhiwUserMockMvc.perform(get("/api/ihiw-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ihiwUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
    }
    
    @Test
    @Transactional
    public void getIhiwUser() throws Exception {
        // Initialize the database
        ihiwUserRepository.saveAndFlush(ihiwUser);

        // Get the ihiwUser
        restIhiwUserMockMvc.perform(get("/api/ihiw-users/{id}", ihiwUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ihiwUser.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIhiwUser() throws Exception {
        // Get the ihiwUser
        restIhiwUserMockMvc.perform(get("/api/ihiw-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIhiwUser() throws Exception {
        // Initialize the database
        ihiwUserRepository.saveAndFlush(ihiwUser);

        int databaseSizeBeforeUpdate = ihiwUserRepository.findAll().size();

        // Update the ihiwUser
        IhiwUser updatedIhiwUser = ihiwUserRepository.findById(ihiwUser.getId()).get();
        // Disconnect from session so that the updates on updatedIhiwUser are not directly saved in db
        em.detach(updatedIhiwUser);
        updatedIhiwUser
            .phone(UPDATED_PHONE);

        restIhiwUserMockMvc.perform(put("/api/ihiw-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIhiwUser)))
            .andExpect(status().isOk());

        // Validate the IhiwUser in the database
        List<IhiwUser> ihiwUserList = ihiwUserRepository.findAll();
        assertThat(ihiwUserList).hasSize(databaseSizeBeforeUpdate);
        IhiwUser testIhiwUser = ihiwUserList.get(ihiwUserList.size() - 1);
        assertThat(testIhiwUser.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingIhiwUser() throws Exception {
        int databaseSizeBeforeUpdate = ihiwUserRepository.findAll().size();

        // Create the IhiwUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIhiwUserMockMvc.perform(put("/api/ihiw-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ihiwUser)))
            .andExpect(status().isBadRequest());

        // Validate the IhiwUser in the database
        List<IhiwUser> ihiwUserList = ihiwUserRepository.findAll();
        assertThat(ihiwUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteIhiwUser() throws Exception {
        // Initialize the database
        ihiwUserRepository.saveAndFlush(ihiwUser);

        int databaseSizeBeforeDelete = ihiwUserRepository.findAll().size();

        // Delete the ihiwUser
        restIhiwUserMockMvc.perform(delete("/api/ihiw-users/{id}", ihiwUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IhiwUser> ihiwUserList = ihiwUserRepository.findAll();
        assertThat(ihiwUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IhiwUser.class);
        IhiwUser ihiwUser1 = new IhiwUser();
        ihiwUser1.setId(1L);
        IhiwUser ihiwUser2 = new IhiwUser();
        ihiwUser2.setId(ihiwUser1.getId());
        assertThat(ihiwUser1).isEqualTo(ihiwUser2);
        ihiwUser2.setId(2L);
        assertThat(ihiwUser1).isNotEqualTo(ihiwUser2);
        ihiwUser1.setId(null);
        assertThat(ihiwUser1).isNotEqualTo(ihiwUser2);
    }
}
