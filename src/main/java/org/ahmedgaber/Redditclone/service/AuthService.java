package org.ahmedgaber.Redditclone.service;


import lombok.AllArgsConstructor;
import org.ahmedgaber.Redditclone.dao.RegisterRequest;
import org.ahmedgaber.Redditclone.exceptions.SpringRedditException;
import org.ahmedgaber.Redditclone.model.NotificationEmail;
import org.ahmedgaber.Redditclone.model.User;
import org.ahmedgaber.Redditclone.model.VerificationToken;
import org.ahmedgaber.Redditclone.repository.UserRepository;
import org.ahmedgaber.Redditclone.repository.VerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthService {


    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final MailService mailService;


    @Transactional
    public void signup(RegisterRequest registerRequest) throws SpringRedditException {

        User user = new User();

        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account", user.getEmail()
                , "Thank you for signing up!! Please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) throws SpringRedditException {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }


    private void fetchUserAndEnable(VerificationToken verificationToken) throws SpringRedditException {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }
}
