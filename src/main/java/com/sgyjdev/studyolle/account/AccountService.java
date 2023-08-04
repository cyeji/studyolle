package com.sgyjdev.studyolle.account;

import com.sgyjdev.studyolle.domain.Account;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    /**
     * 새로운 계정 생성
     *
     * @param signUpForm 회원가입 폼
     */
    @Transactional
    public void processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(newAccount);
    }

    /**
     * 새로운 계정 생성
     *
     * @param signUpForm 회원가입 폼
     * @return 계정
     */
    private Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder().email(signUpForm.getEmail()).nickname(signUpForm.getNickname())
            .password(passwordEncoder.encode(signUpForm.getPassword())).studyCreatedByWeb(true).studyEnrollmentResultByWeb(true).studyCreatedByWeb(true)
            .build();
        return accountRepository.save(account);
    }

    /**
     * 회원가입 인증 메일 전송
     *
     * @param newAccount 신규 계정
     */
    private void sendSignUpConfirmEmail(Account newAccount) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(newAccount.getEmail());
        simpleMailMessage.setSubject("스터디올래, 회원가입 인증");
        simpleMailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email=" + newAccount.getEmail());
        javaMailSender.send(simpleMailMessage);
    }

    public String validEmailToken(String token, String email, Model model) {
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        String view = "account/checked-email";
        if (optionalAccount.isEmpty()) {
            model.addAttribute("error", "wrong.email");
            return view;
        }
        Account account = optionalAccount.get();
        if (!account.getEmailCheckToken().equals(token)) {
            model.addAttribute("error", "wrong.token");
            return view;
        }

        account.successEmailVerified();
        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());
        return view;
    }
}
