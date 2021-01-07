package org.ihiw.management.web.rest;


import io.github.jhipster.web.util.ResponseUtil;
import org.ihiw.management.domain.Authority;
import org.ihiw.management.domain.IhiwUser;
import org.ihiw.management.domain.User;
import org.ihiw.management.repository.IhiwUserRepository;
import org.ihiw.management.repository.UserRepository;
import org.ihiw.management.security.AuthoritiesConstants;
import org.ihiw.management.security.BlacklistManager;
import org.ihiw.management.security.SecurityUtils;
import org.ihiw.management.service.MailService;
import org.ihiw.management.service.UserService;
import org.ihiw.management.service.dto.PasswordChangeDTO;
import org.ihiw.management.service.dto.UserDTO;
import org.ihiw.management.web.rest.errors.*;
import org.ihiw.management.web.rest.vm.KeyAndPasswordVM;
import org.ihiw.management.web.rest.vm.ManagedUserVM;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

import static org.ihiw.management.security.AuthoritiesConstants.ADMIN;
import static org.ihiw.management.security.AuthoritiesConstants.PI;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final IhiwUserRepository ihiwUserRepository;

    private final UserService userService;

    private final MailService mailService;

    private final String activationEmail;

    private final BlacklistManager blacklistManager;

    public AccountResource(UserRepository userRepository, IhiwUserRepository ihiwUserRepository, UserService userService, MailService mailService, @Qualifier("activationEmail") String activationEmail, BlacklistManager blacklistManager) {

        this.userRepository = userRepository;
        this.ihiwUserRepository = ihiwUserRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.activationEmail = activationEmail;
        this.blacklistManager = blacklistManager;

    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM, HttpServletRequest request) {
        blacklistManager.checkRequest(request);

        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        if (managedUserVM.isExistingLab()){
            IhiwUser ihiwUser = ihiwUserRepository.findByUser(user);
            List<IhiwUser> labUsers = ihiwUserRepository.findByLab(ihiwUser.getLab());

            boolean mailSent = false;
            //send the activation mail to all PIs of the lab
            for (IhiwUser labUser : labUsers){
                Optional<User> userFromIhiw = userRepository.findOneWithAuthoritiesById(labUser.getUser().getId());
                if (userFromIhiw.get().getAuthorities().contains(new Authority(PI))){
                    mailService.sendActivationEmail(user, labUser.getUser().getEmail(), userFromIhiw.get().getFirstName());
                    mailSent = true;
                }
            }
            if (!mailSent){
                //if there was no PI found, send it to the admin address
                mailService.sendActivationEmail(user, activationEmail, "admin");
            }
        } else {
            mailService.sendActivationEmail(user, activationEmail, "admin");
        }
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */

    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> currentUser = userService.getUserWithAuthorities();
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();

        Optional<User> newUser = userRepository.findOneByActivationKey(key);
        IhiwUser newIhiwUser = ihiwUserRepository.findByUser(newUser.get());

        //admins can register everyone and PIs their staff
        if (currentUser.get().getAuthorities().contains(new Authority(ADMIN)) ||
            (currentUser.get().getAuthorities().contains(new Authority(PI)) && currentIhiwUser.getLab().equals(newIhiwUser.getLab()))){
            Optional<User> user = userService.activateRegistration(key);
            if (!user.isPresent()) {
                throw new AccountResourceException("No user was found for this activation key");
            }
            mailService.sendActivationConfirmation(user.get());
        }
        else {
            throw new AccountResourceException("Not authorized to activate user");
        }

    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public UserDTO getAccount() {
        return userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
            userDTO.getLangKey(), userDTO.getImageUrl());
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     * @throws EmailNotFoundException {@code 400 (Bad Request)} if the email address is not registered.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
       mailService.sendPasswordResetMail(
           userService.requestPasswordReset(mail)
               .orElseThrow(EmailNotFoundException::new)
       );
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user =
            userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
