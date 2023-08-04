package com.sgyjdev.studyolle.account;

import com.sgyjdev.studyolle.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
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

}
