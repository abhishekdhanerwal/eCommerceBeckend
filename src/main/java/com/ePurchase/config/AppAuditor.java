package com.ePurchase.config;

import com.ePurchase.domain.User;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppAuditor implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (isPrincipalPresent(auth) && auth.getPrincipal().getClass().isAssignableFrom(User.class)) {
            return ((User) auth.getPrincipal()).getUserName();
        } else if (isPrincipalPresent(auth) && auth.getPrincipal().getClass().isAssignableFrom(String.class)) {
        	return (String) auth.getPrincipal();
        }
        else
        {
        return "System";
        }
    }

    private boolean isPrincipalPresent(Authentication auth) {
        return auth != null && auth.getPrincipal() != null;
    }
}

