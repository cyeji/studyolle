package com.sgyjdev.studyolle.main;

import com.sgyjdev.studyolle.account.CurrentUser;
import com.sgyjdev.studyolle.domain.Account;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        if (Optional.ofNullable(account).isPresent()) {
            model.addAttribute(account);
            return "index-after-login";
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
