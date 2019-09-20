package org.ihiw.management.web.rest;

import org.ihiw.management.IhiwManagementApp;
import org.ihiw.management.domain.IhiwLab;
import org.ihiw.management.repository.IhiwLabRepository;
import org.ihiw.management.repository.IhiwUserRepository;
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

/**
 * Integration tests for the {@link IhiwLabResource} REST controller.
 */
@SpringBootTest(classes = IhiwManagementApp.class)
public class IhiwLabResourceIT {

    private static final String DEFAULT_LAB_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LAB_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTOR = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTION = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_S_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_S_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_S_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_S_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP = "AAAAAAAAAA";
    private static final String UPDATED_ZIP = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_FAX = "AAAAAAAAAA";
    private static final String UPDATED_FAX = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_OLD_LAB_CODE = "AAAAAAAAAA";
    private static final String UPDATED_OLD_LAB_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_S_NAME = "AAAAAAAAAA";
    private static final String UPDATED_S_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_S_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_S_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_S_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_S_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_D_NAME = "AAAAAAAAAA";
    private static final String UPDATED_D_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_D_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_D_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_D_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_D_PHONE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private IhiwLabRepository ihiwLabRepository;

    @Autowired
    private IhiwUserRepository ihiwUserRepository;

    @Autowired
    private UserService userService;

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

    private MockMvc restIhiwLabMockMvc;

    private IhiwLab ihiwLab;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IhiwLabResource ihiwLabResource = new IhiwLabResource(ihiwLabRepository, ihiwUserRepository, userService);
        this.restIhiwLabMockMvc = MockMvcBuilders.standaloneSetup(ihiwLabResource)
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
    public static IhiwLab createEntity(EntityManager em) {
        IhiwLab ihiwLab = new IhiwLab()
            .labCode(DEFAULT_LAB_CODE)
            .title(DEFAULT_TITLE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .director(DEFAULT_DIRECTOR)
            .department(DEFAULT_DEPARTMENT)
            .institution(DEFAULT_INSTITUTION)
            .address1(DEFAULT_ADDRESS_1)
            .address2(DEFAULT_ADDRESS_2)
            .sAddress1(DEFAULT_S_ADDRESS_1)
            .sAddress(DEFAULT_S_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zip(DEFAULT_ZIP)
            .country(DEFAULT_COUNTRY)
            .phone(DEFAULT_PHONE)
            .fax(DEFAULT_FAX)
            .email(DEFAULT_EMAIL)
            .url(DEFAULT_URL)
            .oldLabCode(DEFAULT_OLD_LAB_CODE)
            .sName(DEFAULT_S_NAME)
            .sPhone(DEFAULT_S_PHONE)
            .sEmail(DEFAULT_S_EMAIL)
            .dName(DEFAULT_D_NAME)
            .dEmail(DEFAULT_D_EMAIL)
            .dPhone(DEFAULT_D_PHONE)
            .createdAt(DEFAULT_CREATED_AT);
        return ihiwLab;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IhiwLab createUpdatedEntity(EntityManager em) {
        IhiwLab ihiwLab = new IhiwLab()
            .labCode(UPDATED_LAB_CODE)
            .title(UPDATED_TITLE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .director(UPDATED_DIRECTOR)
            .department(UPDATED_DEPARTMENT)
            .institution(UPDATED_INSTITUTION)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .sAddress1(UPDATED_S_ADDRESS_1)
            .sAddress(UPDATED_S_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zip(UPDATED_ZIP)
            .country(UPDATED_COUNTRY)
            .phone(UPDATED_PHONE)
            .fax(UPDATED_FAX)
            .email(UPDATED_EMAIL)
            .url(UPDATED_URL)
            .oldLabCode(UPDATED_OLD_LAB_CODE)
            .sName(UPDATED_S_NAME)
            .sPhone(UPDATED_S_PHONE)
            .sEmail(UPDATED_S_EMAIL)
            .dName(UPDATED_D_NAME)
            .dEmail(UPDATED_D_EMAIL)
            .dPhone(UPDATED_D_PHONE)
            .createdAt(UPDATED_CREATED_AT);
        return ihiwLab;
    }

    @BeforeEach
    public void initTest() {
        ihiwLab = createEntity(em);
    }

    @Test
    @Transactional
    public void createIhiwLab() throws Exception {
        int databaseSizeBeforeCreate = ihiwLabRepository.findAll().size();

        // Create the IhiwLab
        restIhiwLabMockMvc.perform(post("/api/ihiw-labs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ihiwLab)))
            .andExpect(status().isCreated());

        // Validate the IhiwLab in the database
        List<IhiwLab> ihiwLabList = ihiwLabRepository.findAll();
        assertThat(ihiwLabList).hasSize(databaseSizeBeforeCreate + 1);
        IhiwLab testIhiwLab = ihiwLabList.get(ihiwLabList.size() - 1);
        assertThat(testIhiwLab.getLabCode()).isEqualTo(DEFAULT_LAB_CODE);
        assertThat(testIhiwLab.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testIhiwLab.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testIhiwLab.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testIhiwLab.getDirector()).isEqualTo(DEFAULT_DIRECTOR);
        assertThat(testIhiwLab.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testIhiwLab.getInstitution()).isEqualTo(DEFAULT_INSTITUTION);
        assertThat(testIhiwLab.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testIhiwLab.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testIhiwLab.getsAddress1()).isEqualTo(DEFAULT_S_ADDRESS_1);
        assertThat(testIhiwLab.getsAddress()).isEqualTo(DEFAULT_S_ADDRESS);
        assertThat(testIhiwLab.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testIhiwLab.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testIhiwLab.getZip()).isEqualTo(DEFAULT_ZIP);
        assertThat(testIhiwLab.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testIhiwLab.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testIhiwLab.getFax()).isEqualTo(DEFAULT_FAX);
        assertThat(testIhiwLab.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testIhiwLab.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testIhiwLab.getOldLabCode()).isEqualTo(DEFAULT_OLD_LAB_CODE);
        assertThat(testIhiwLab.getsName()).isEqualTo(DEFAULT_S_NAME);
        assertThat(testIhiwLab.getsPhone()).isEqualTo(DEFAULT_S_PHONE);
        assertThat(testIhiwLab.getsEmail()).isEqualTo(DEFAULT_S_EMAIL);
        assertThat(testIhiwLab.getdName()).isEqualTo(DEFAULT_D_NAME);
        assertThat(testIhiwLab.getdEmail()).isEqualTo(DEFAULT_D_EMAIL);
        assertThat(testIhiwLab.getdPhone()).isEqualTo(DEFAULT_D_PHONE);
        assertThat(testIhiwLab.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createIhiwLabWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ihiwLabRepository.findAll().size();

        // Create the IhiwLab with an existing ID
        ihiwLab.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIhiwLabMockMvc.perform(post("/api/ihiw-labs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ihiwLab)))
            .andExpect(status().isBadRequest());

        // Validate the IhiwLab in the database
        List<IhiwLab> ihiwLabList = ihiwLabRepository.findAll();
        assertThat(ihiwLabList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLabCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ihiwLabRepository.findAll().size();
        // set the field null
        ihiwLab.setLabCode(null);

        // Create the IhiwLab, which fails.

        restIhiwLabMockMvc.perform(post("/api/ihiw-labs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ihiwLab)))
            .andExpect(status().isBadRequest());

        List<IhiwLab> ihiwLabList = ihiwLabRepository.findAll();
        assertThat(ihiwLabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ihiwLabRepository.findAll().size();
        // set the field null
        ihiwLab.setFirstName(null);

        // Create the IhiwLab, which fails.

        restIhiwLabMockMvc.perform(post("/api/ihiw-labs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ihiwLab)))
            .andExpect(status().isBadRequest());

        List<IhiwLab> ihiwLabList = ihiwLabRepository.findAll();
        assertThat(ihiwLabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ihiwLabRepository.findAll().size();
        // set the field null
        ihiwLab.setLastName(null);

        // Create the IhiwLab, which fails.

        restIhiwLabMockMvc.perform(post("/api/ihiw-labs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ihiwLab)))
            .andExpect(status().isBadRequest());

        List<IhiwLab> ihiwLabList = ihiwLabRepository.findAll();
        assertThat(ihiwLabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = ihiwLabRepository.findAll().size();
        // set the field null
        ihiwLab.setEmail(null);

        // Create the IhiwLab, which fails.

        restIhiwLabMockMvc.perform(post("/api/ihiw-labs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ihiwLab)))
            .andExpect(status().isBadRequest());

        List<IhiwLab> ihiwLabList = ihiwLabRepository.findAll();
        assertThat(ihiwLabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIhiwLabs() throws Exception {
        // Initialize the database
        ihiwLabRepository.saveAndFlush(ihiwLab);

        // Get all the ihiwLabList
        restIhiwLabMockMvc.perform(get("/api/ihiw-labs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ihiwLab.getId().intValue())))
            .andExpect(jsonPath("$.[*].labCode").value(hasItem(DEFAULT_LAB_CODE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].director").value(hasItem(DEFAULT_DIRECTOR.toString())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT.toString())))
            .andExpect(jsonPath("$.[*].institution").value(hasItem(DEFAULT_INSTITUTION.toString())))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].sAddress1").value(hasItem(DEFAULT_S_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].sAddress").value(hasItem(DEFAULT_S_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].oldLabCode").value(hasItem(DEFAULT_OLD_LAB_CODE.toString())))
            .andExpect(jsonPath("$.[*].sName").value(hasItem(DEFAULT_S_NAME.toString())))
            .andExpect(jsonPath("$.[*].sPhone").value(hasItem(DEFAULT_S_PHONE.toString())))
            .andExpect(jsonPath("$.[*].sEmail").value(hasItem(DEFAULT_S_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].dName").value(hasItem(DEFAULT_D_NAME.toString())))
            .andExpect(jsonPath("$.[*].dEmail").value(hasItem(DEFAULT_D_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].dPhone").value(hasItem(DEFAULT_D_PHONE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }

    @Test
    @Transactional
    public void getIhiwLab() throws Exception {
        // Initialize the database
        ihiwLabRepository.saveAndFlush(ihiwLab);

        // Get the ihiwLab
        restIhiwLabMockMvc.perform(get("/api/ihiw-labs/{id}", ihiwLab.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ihiwLab.getId().intValue()))
            .andExpect(jsonPath("$.labCode").value(DEFAULT_LAB_CODE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.director").value(DEFAULT_DIRECTOR.toString()))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT.toString()))
            .andExpect(jsonPath("$.institution").value(DEFAULT_INSTITUTION.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.sAddress1").value(DEFAULT_S_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.sAddress").value(DEFAULT_S_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.fax").value(DEFAULT_FAX.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.oldLabCode").value(DEFAULT_OLD_LAB_CODE.toString()))
            .andExpect(jsonPath("$.sName").value(DEFAULT_S_NAME.toString()))
            .andExpect(jsonPath("$.sPhone").value(DEFAULT_S_PHONE.toString()))
            .andExpect(jsonPath("$.sEmail").value(DEFAULT_S_EMAIL.toString()))
            .andExpect(jsonPath("$.dName").value(DEFAULT_D_NAME.toString()))
            .andExpect(jsonPath("$.dEmail").value(DEFAULT_D_EMAIL.toString()))
            .andExpect(jsonPath("$.dPhone").value(DEFAULT_D_PHONE.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }

    @Test
    @Transactional
    public void getNonExistingIhiwLab() throws Exception {
        // Get the ihiwLab
        restIhiwLabMockMvc.perform(get("/api/ihiw-labs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIhiwLab() throws Exception {
        // Initialize the database
        ihiwLabRepository.saveAndFlush(ihiwLab);

        int databaseSizeBeforeUpdate = ihiwLabRepository.findAll().size();

        // Update the ihiwLab
        IhiwLab updatedIhiwLab = ihiwLabRepository.findById(ihiwLab.getId()).get();
        // Disconnect from session so that the updates on updatedIhiwLab are not directly saved in db
        em.detach(updatedIhiwLab);
        updatedIhiwLab
            .labCode(UPDATED_LAB_CODE)
            .title(UPDATED_TITLE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .director(UPDATED_DIRECTOR)
            .department(UPDATED_DEPARTMENT)
            .institution(UPDATED_INSTITUTION)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .sAddress1(UPDATED_S_ADDRESS_1)
            .sAddress(UPDATED_S_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zip(UPDATED_ZIP)
            .country(UPDATED_COUNTRY)
            .phone(UPDATED_PHONE)
            .fax(UPDATED_FAX)
            .email(UPDATED_EMAIL)
            .url(UPDATED_URL)
            .oldLabCode(UPDATED_OLD_LAB_CODE)
            .sName(UPDATED_S_NAME)
            .sPhone(UPDATED_S_PHONE)
            .sEmail(UPDATED_S_EMAIL)
            .dName(UPDATED_D_NAME)
            .dEmail(UPDATED_D_EMAIL)
            .dPhone(UPDATED_D_PHONE)
            .createdAt(UPDATED_CREATED_AT);

        restIhiwLabMockMvc.perform(put("/api/ihiw-labs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIhiwLab)))
            .andExpect(status().isOk());

        // Validate the IhiwLab in the database
        List<IhiwLab> ihiwLabList = ihiwLabRepository.findAll();
        assertThat(ihiwLabList).hasSize(databaseSizeBeforeUpdate);
        IhiwLab testIhiwLab = ihiwLabList.get(ihiwLabList.size() - 1);
        assertThat(testIhiwLab.getLabCode()).isEqualTo(UPDATED_LAB_CODE);
        assertThat(testIhiwLab.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testIhiwLab.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testIhiwLab.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testIhiwLab.getDirector()).isEqualTo(UPDATED_DIRECTOR);
        assertThat(testIhiwLab.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testIhiwLab.getInstitution()).isEqualTo(UPDATED_INSTITUTION);
        assertThat(testIhiwLab.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testIhiwLab.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testIhiwLab.getsAddress1()).isEqualTo(UPDATED_S_ADDRESS_1);
        assertThat(testIhiwLab.getsAddress()).isEqualTo(UPDATED_S_ADDRESS);
        assertThat(testIhiwLab.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testIhiwLab.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testIhiwLab.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testIhiwLab.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testIhiwLab.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testIhiwLab.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testIhiwLab.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testIhiwLab.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testIhiwLab.getOldLabCode()).isEqualTo(UPDATED_OLD_LAB_CODE);
        assertThat(testIhiwLab.getsName()).isEqualTo(UPDATED_S_NAME);
        assertThat(testIhiwLab.getsPhone()).isEqualTo(UPDATED_S_PHONE);
        assertThat(testIhiwLab.getsEmail()).isEqualTo(UPDATED_S_EMAIL);
        assertThat(testIhiwLab.getdName()).isEqualTo(UPDATED_D_NAME);
        assertThat(testIhiwLab.getdEmail()).isEqualTo(UPDATED_D_EMAIL);
        assertThat(testIhiwLab.getdPhone()).isEqualTo(UPDATED_D_PHONE);
        assertThat(testIhiwLab.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingIhiwLab() throws Exception {
        int databaseSizeBeforeUpdate = ihiwLabRepository.findAll().size();

        // Create the IhiwLab

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIhiwLabMockMvc.perform(put("/api/ihiw-labs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ihiwLab)))
            .andExpect(status().isBadRequest());

        // Validate the IhiwLab in the database
        List<IhiwLab> ihiwLabList = ihiwLabRepository.findAll();
        assertThat(ihiwLabList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteIhiwLab() throws Exception {
        // Initialize the database
        ihiwLabRepository.saveAndFlush(ihiwLab);

        int databaseSizeBeforeDelete = ihiwLabRepository.findAll().size();

        // Delete the ihiwLab
        restIhiwLabMockMvc.perform(delete("/api/ihiw-labs/{id}", ihiwLab.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IhiwLab> ihiwLabList = ihiwLabRepository.findAll();
        assertThat(ihiwLabList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IhiwLab.class);
        IhiwLab ihiwLab1 = new IhiwLab();
        ihiwLab1.setId(1L);
        IhiwLab ihiwLab2 = new IhiwLab();
        ihiwLab2.setId(ihiwLab1.getId());
        assertThat(ihiwLab1).isEqualTo(ihiwLab2);
        ihiwLab2.setId(2L);
        assertThat(ihiwLab1).isNotEqualTo(ihiwLab2);
        ihiwLab1.setId(null);
        assertThat(ihiwLab1).isNotEqualTo(ihiwLab2);
    }
}
