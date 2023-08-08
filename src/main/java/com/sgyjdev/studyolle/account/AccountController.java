package com.sgyjdev.studyolle.account;

import com.sgyjdev.studyolle.domain.Account;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            goSignUp();
        }

        Account account = accountService.processNewAccount(signUpForm);
        return "redirect:/";
    }

    public String goSignUp() {
        return "account/sign-up";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        return accountService.validEmailToken(token, email, model);
    }

    @GetMapping("/check-email")
    public String checkEmail(@CurrentUser Account account, Model model) {
        if (Optional.ofNullable(account).isPresent()) {
            model.addAttribute("email", account.getEmail());
        }
        return "account/check-email";
    }

    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(@CurrentUser Account account, Model model) {
        if (!account.canSendConfirmEmail()) {
            model.addAttribute("error", "인증 이메일은 1시간에 한번만 전송할 수 있습니다.");
            model.addAttribute(account);
            return "account/check-email";
        }

        accountService.sendSignUpConfirmEmail(account);
        return "redirect:/";
    }

}
