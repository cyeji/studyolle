package com.sgyjdev.studyolle.account;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sgyjdev.studyolle.domain.Account;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    @DisplayName("회원 가입 화면 보이는지 테스트")
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up")).andExpect(status().isOk()).andExpect(view().name("account/sign-up")).andExpect(model().attributeExists("signUpForm"));
    }


    @Test
    @DisplayName("회원가입처리 - 입력값 오류")
    void signUpSubmit_with_wrong_input() throws Exception {
        String mail = "choyeji@gmail.com";
        mockMvc.perform(post("/sign-up").param("nickname", "yeji").param("email", mail).param("password", "12").with(csrf())).andExpect(status().isOk())
            .andExpect(view().name("account/sign-up"));

    }

    @Test
    @DisplayName("회원가입처리 - 입력값 정상")
    void signUpSubmit_with_correct_input() throws Exception {
        String mail = "seunggu@gmail.com";
        mockMvc.perform(post("/sign-up").param("nickname", "seunggu").param("email", mail).param("password", "12345678").with(csrf()))
            .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/"));

        Optional<Account> optionalAccount = accountRepository.findByEmail(mail);
        assertNotNull(optionalAccount);
        assertNotEquals("12345678", optionalAccount.get().getPassword());

        assertTrue(accountRepository.existsByEmail(mail));
        // 이메일 전송 여부 확인
//        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }

}