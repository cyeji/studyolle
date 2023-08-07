package com.sgyjdev.studyolle.config;

import com.sgyjdev.studyolle.account.AccountRepository;
import com.sgyjdev.studyolle.account.UserAccount;
import com.sgyjdev.studyolle.domain.Account;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByEmail(emailOrNickname);
        if (optionalAccount.isEmpty()) {
            optionalAccount = accountRepository.findByNickname(emailOrNickname);
        }
        if (optionalAccount.isEmpty()) {
            throw new UsernameNotFoundException("email or nickname not found : " + emailOrNickname);
        }

        return UserAccount.from(optionalAccount.get());
    }
}
