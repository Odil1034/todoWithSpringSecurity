package uz.pdp.maven.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.pdp.maven.model.AuthUser;

@Component
public class SessionUser {

    public AuthUser getUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null &&
                authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            AuthUser authUser = userDetails.getAuthUser();
            System.out.println(authUser);
            return authUser;
        }
        return null;
    }

}