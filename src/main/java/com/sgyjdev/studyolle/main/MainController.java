package com.sgyjdev.studyolle.main;

import com.sgyjdev.studyolle.account.CurrentUser;
import com.sgyjdev.studyolle.domain.Account;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.error("Authentication :: {}", authentication);
        if (Optional.ofNullable(account).isPresent()) {
            model.addAttribute(account);
            return "index-after-login";
        }

        log.info("account : {}", account);

        return "index";
    }

}
