package org.ihiw.management.web.rest;

import org.ihiw.management.IhiwManagementApp;
import org.ihiw.management.domain.Upload;
import org.ihiw.management.repository.*;
import org.ihiw.management.service.UploadService;
import org.ihiw.management.service.UserService;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.ihiw.management.web.rest.TestUtil.sameInstant;
import static org.ihiw.management.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.ihiw.management.domain.enumeration.FileType;
/**
 * Integration tests for the {@link UploadResource} REST controller.
 */
@SpringBootTest(classes = IhiwManagementApp.class)
public class UploadResourceIT {

    private static final FileType DEFAULT_TYPE = FileType.HAML;
    private static final FileType UPDATED_TYPE = FileType.HML;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_MODIFIED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MODIFIED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VALID = false;
    private static final Boolean UPDATED_VALID = true;

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private UploadRepository uploadRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private IhiwUserRepository ihiwUserRepository;

    @Autowired
    private ValidationRepository validationRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectIhiwLabRepository projectIhiwLabRepository;

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

    @Autowired
    private UserService userService;

    @Autowired
    private UploadService uploadService;

    private MockMvc restUploadMockMvc;

    private Upload upload;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UploadResource uploadResource = new UploadResource(uploadRepository, uploadService, fileRepository, ihiwUserRepository, validationRepository, userService, projectRepository, projectIhiwLabRepository);
        this.restUploadMockMvc = MockMvcBuilders.standaloneSetup(uploadResource)
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
    public static Upload createEntity(EntityManager em) {
        Upload upload = new Upload()
            .type(DEFAULT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .modifiedAt(DEFAULT_MODIFIED_AT)
            .fileName(DEFAULT_FILE_NAME)
            .enabled(DEFAULT_ENABLED);
        return upload;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Upload createUpdatedEntity(EntityManager em) {
        Upload upload = new Upload()
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .fileName(UPDATED_FILE_NAME)
            .enabled(UPDATED_ENABLED);
        return upload;
    }

    @BeforeEach
    public void initTest() {
        upload = createEntity(em);
    }

    @Test
    @Transactional
    public void createUpload() throws Exception {
        int databaseSizeBeforeCreate = uploadRepository.findAll().size();

        // Create the Upload
        restUploadMockMvc.perform(post("/api/uploads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(upload)))
            .andExpect(status().isCreated());

        // Validate the Upload in the database
        List<Upload> uploadList = uploadRepository.findAll();
        assertThat(uploadList).hasSize(databaseSizeBeforeCreate + 1);
        Upload testUpload = uploadList.get(uploadList.size() - 1);
        assertThat(testUpload.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testUpload.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUpload.getModifiedAt()).isEqualTo(DEFAULT_MODIFIED_AT);
        assertThat(testUpload.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testUpload.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createUploadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = uploadRepository.findAll().size();

        // Create the Upload with an existing ID
        upload.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUploadMockMvc.perform(post("/api/uploads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(upload)))
            .andExpect(status().isBadRequest());

        // Validate the Upload in the database
        List<Upload> uploadList = uploadRepository.findAll();
        assertThat(uploadList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUploads() throws Exception {
        // Initialize the database
        uploadRepository.saveAndFlush(upload);

        // Get all the uploadList
        restUploadMockMvc.perform(get("/api/uploads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(upload.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(sameInstant(DEFAULT_MODIFIED_AT))))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].valid").value(hasItem(DEFAULT_VALID.booleanValue())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getUpload() throws Exception {
        // Initialize the database
        uploadRepository.saveAndFlush(upload);

        // Get the upload
        restUploadMockMvc.perform(get("/api/uploads/{id}", upload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(upload.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.modifiedAt").value(sameInstant(DEFAULT_MODIFIED_AT)))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.valid").value(DEFAULT_VALID.booleanValue()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUpload() throws Exception {
        // Get the upload
        restUploadMockMvc.perform(get("/api/uploads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUpload() throws Exception {
        // Initialize the database
        uploadRepository.saveAndFlush(upload);

        int databaseSizeBeforeUpdate = uploadRepository.findAll().size();

        // Update the upload
        Upload updatedUpload = uploadRepository.findById(upload.getId()).get();
        // Disconnect from session so that the updates on updatedUpload are not directly saved in db
        em.detach(updatedUpload);
        updatedUpload
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .fileName(UPDATED_FILE_NAME)
            .enabled(UPDATED_ENABLED);

        restUploadMockMvc.perform(put("/api/uploads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUpload)))
            .andExpect(status().isOk());

        // Validate the Upload in the database
        List<Upload> uploadList = uploadRepository.findAll();
        assertThat(uploadList).hasSize(databaseSizeBeforeUpdate);
        Upload testUpload = uploadList.get(uploadList.size() - 1);
        assertThat(testUpload.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUpload.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUpload.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
        assertThat(testUpload.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testUpload.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingUpload() throws Exception {
        int databaseSizeBeforeUpdate = uploadRepository.findAll().size();

        // Create the Upload

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUploadMockMvc.perform(put("/api/uploads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(upload)))
            .andExpect(status().isBadRequest());

        // Validate the Upload in the database
        List<Upload> uploadList = uploadRepository.findAll();
        assertThat(uploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUpload() throws Exception {
        // Initialize the database
        uploadRepository.saveAndFlush(upload);

        int databaseSizeBeforeDelete = uploadRepository.findAll().size();

        // Delete the upload
        restUploadMockMvc.perform(delete("/api/uploads/{id}", upload.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Upload> uploadList = uploadRepository.findAll();
        assertThat(uploadList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Upload.class);
        Upload upload1 = new Upload();
        upload1.setId(1L);
        Upload upload2 = new Upload();
        upload2.setId(upload1.getId());
        assertThat(upload1).isEqualTo(upload2);
        upload2.setId(2L);
        assertThat(upload1).isNotEqualTo(upload2);
        upload1.setId(null);
        assertThat(upload1).isNotEqualTo(upload2);
    }
}
