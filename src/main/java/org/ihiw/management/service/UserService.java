package org.ihiw.management.service;

import org.ihiw.management.config.Constants;
import org.ihiw.management.domain.Authority;
import org.ihiw.management.domain.IhiwLab;
import org.ihiw.management.domain.IhiwUser;
import org.ihiw.management.domain.User;
import org.ihiw.management.repository.*;
import org.ihiw.management.security.AuthoritiesConstants;
import org.ihiw.management.security.SecurityUtils;
import org.ihiw.management.service.dto.ProjectDTO;
import org.ihiw.management.service.dto.UploadDTO;
import org.ihiw.management.service.dto.UserDTO;
import org.ihiw.management.service.util.RandomUtil;
import org.ihiw.management.web.rest.errors.EmailAlreadyUsedException;
import org.ihiw.management.web.rest.errors.InvalidPasswordException;
import org.ihiw.management.web.rest.errors.LabDoesNotExistException;
import org.ihiw.management.web.rest.errors.LoginAlreadyUsedException;
import org.ihiw.management.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    private final IhiwUserRepository ihiwUserRepository;

    private final IhiwLabRepository ihiwLabRepository;

    private final UploadRepository uploadRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    public UserService(UploadRepository uploadRepository, UserRepository userRepository, ProjectRepository projectRepository, IhiwUserRepository ihiwUserRepository, IhiwLabRepository ihiwLabRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.ihiwLabRepository = ihiwLabRepository;
        this.ihiwUserRepository = ihiwUserRepository;
        this.projectRepository = projectRepository;
        this.uploadRepository = uploadRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .map(user -> {
                user.setActivated(true);
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(ManagedUserVM managedUserVM, String password) {
        userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new LoginAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(managedUserVM.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(managedUserVM.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(managedUserVM.getFirstName());
        newUser.setLastName(managedUserVM.getLastName());
        newUser.setEmail(managedUserVM.getEmail().toLowerCase());
        newUser.setImageUrl(managedUserVM.getImageUrl());
        newUser.setLangKey(managedUserVM.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        if (managedUserVM.isExistingLab()){
            authorityRepository.findById(AuthoritiesConstants.LAB_MEMBER).ifPresent(authorities::add);
        } else {
            authorityRepository.findById(AuthoritiesConstants.PI).ifPresent(authorities::add);
        }
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);

        IhiwUser ihiwUser = new IhiwUser();
        ihiwUser.setUser(newUser);
        ihiwUser.setPhone(managedUserVM.getPhone());
        ihiwUser = ihiwUserRepository.save(ihiwUser);

        IhiwLab lab;
        if (managedUserVM.isExistingLab() ){
            if (managedUserVM.getLabCode() == null || managedUserVM.getLabCode().isEmpty()){
                throw new LabDoesNotExistException();
            }
            Optional<IhiwLab> theLab = ihiwLabRepository.findByLabCode(managedUserVM.getLabCode());
            if (theLab.isPresent()){
                lab = theLab.get();
            }
            else {
                throw new LabDoesNotExistException();
            }
        } else {
            lab = new IhiwLab();
            String labCode = "";

            if (managedUserVM.getTitle() != null && !managedUserVM.getTitle().isEmpty()){
                lab.setTitle(managedUserVM.getTitle());
            }
            if (managedUserVM.getFirstName() != null && !managedUserVM.getFirstName().isEmpty()){
                lab.setFirstName(managedUserVM.getFirstName());
            }
            if (managedUserVM.getDepartment() != null && !managedUserVM.getDepartment().isEmpty()){
                lab.setDepartment(managedUserVM.getDepartment());
            }
            if (managedUserVM.getInstitution() != null && !managedUserVM.getInstitution().isEmpty()){
                lab.setInstitution(managedUserVM.getInstitution());
            }
            if (managedUserVM.getAddress1() != null && !managedUserVM.getAddress1().isEmpty()){
                lab.setAddress1(managedUserVM.getAddress1());
            }
            if (managedUserVM.getAddress2() != null && !managedUserVM.getAddress2().isEmpty()){
                lab.setAddress2(managedUserVM.getAddress1());
            }
            if (managedUserVM.getZip() != null && !managedUserVM.getZip().isEmpty()){
                lab.setZip(managedUserVM.getZip());
            }
            if (managedUserVM.getCountry() != null && !managedUserVM.getCountry().isEmpty()){
                lab.setCountry(managedUserVM.getCountry());
                if (lab.getCountry().equals("United States")){
                    labCode += "U";
                }
                else {
                    labCode += managedUserVM.getCountryCode();
                }
            }
            if (managedUserVM.getState() != null && !managedUserVM.getState().isEmpty()){
                lab.setState(managedUserVM.getState());
                if (lab.getCountry().equals("United States")){
                    labCode += lab.getState().trim().substring(0,2);
                }
            }
            if (managedUserVM.getLastName() != null && !managedUserVM.getLastName().isEmpty()){
                lab.setLastName(managedUserVM.getLastName());
                labCode += lab.getLastName().trim().substring(0,3);
            }
            if (managedUserVM.getPhone() != null && !managedUserVM.getPhone().isEmpty()){
                lab.setPhone(managedUserVM.getPhone());
            }
            if (managedUserVM.getFax() != null && !managedUserVM.getFax().isEmpty()){
                lab.setFax(managedUserVM.getFax());
            }
            if (managedUserVM.getDirector() != null && !managedUserVM.getDirector().isEmpty()){
                lab.setDirector(managedUserVM.getDirector());
            }
            if (managedUserVM.getCity() != null && !managedUserVM.getCity().isEmpty()){
                lab.setCity(managedUserVM.getCity());
            }
            if (managedUserVM.getUrl() != null && !managedUserVM.getUrl().isEmpty()){
                lab.setUrl(managedUserVM.getUrl());
            }
            if (managedUserVM.getLabEmail() != null && !managedUserVM.getLabEmail().isEmpty()){
                lab.setEmail(managedUserVM.getLabEmail());
            }
            if (managedUserVM.getTitle() != null && !managedUserVM.getTitle().isEmpty()){
                lab.setTitle(managedUserVM.getTitle());
            }

            lab.setLabCode(labCode.replace(" ", "").toUpperCase());

            lab.setCreatedAt(ZonedDateTime.now());

            ihiwLabRepository.save(lab);
        }
        ihiwUser.setLab(lab);
        ihiwUserRepository.save(ihiwUser);

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser){
        if (existingUser.getActivated()) {
             return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail().toLowerCase());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email.toLowerCase());
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail().toLowerCase());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getMyManagedUsers(Pageable pageable, IhiwLab ihiwLab) {
        List<IhiwUser> users = ihiwUserRepository.findByLab(ihiwLab);
        List<Long> ids = new ArrayList<>();
        for (IhiwUser user : users){
            ids.add(user.getUser().getId());
        }
        return userRepository.findByUserInIds(pageable, ids).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UploadDTO> getAllUploads(Pageable pageable) {
        return  uploadRepository.findAll(pageable).map(UploadDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UploadDTO> getAllUploadsByUserId(Pageable pageable,List<Long> ids) {
        return  uploadRepository.findAllUploadsByUserId(pageable,ids).map(UploadDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Page<ProjectDTO> getAllProjects(Pageable pageable) {
        return  projectRepository.findAll(pageable).map(ProjectDTO::new);
    }


    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }


    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
    }
}
