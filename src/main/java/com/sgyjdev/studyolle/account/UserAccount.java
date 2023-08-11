package com.sgyjdev.studyolle.account;

import com.sgyjdev.studyolle.domain.Account;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class UserAccount extends User {

    private Account account;

    UserAccount(Account account) {
        super(account.getNickname(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.account = account;
    }

    public static UserAccount from(Account account) {
        return new UserAccount(account);
    }
}
