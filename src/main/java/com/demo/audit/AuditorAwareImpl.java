package com.demo.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        log.debug("Inside AuditorAwareImpl - getCurrentAuditor()");

        try{
            log.debug("Inside Try Block of AuditorAwareImpl");
            return Optional.ofNullable(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        }catch (Exception e){

            log.debug("Inside Catch Block of AuditorAwareImpl");
            return Optional.empty();
        }
    }
}
