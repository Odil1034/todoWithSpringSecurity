package uz.pdp.maven.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.maven.model.AuthUser;
import uz.pdp.maven.model.Permission;
import uz.pdp.maven.model.Role;
import uz.pdp.maven.repository.PermissionRepo;
import uz.pdp.maven.repository.RoleRepo;
import uz.pdp.maven.repository.UserRepo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;
    private final PermissionRepo permissionRepo;
    private final RoleRepo roleRepo;

    public CustomUserDetailsService(UserRepo userRepo, PermissionRepo permissionRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.permissionRepo = permissionRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = userRepo.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username %s".formatted(username)));

        Integer userId = authUser.getId();
        List<Role> roles = roleRepo.getByUserId(userId);

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
            List<Permission> permissions = permissionRepo.getByRoleId(role.getId());
            for (Permission permission : permissions) {
                role.setPermissions(permissions);
                authorities.add(new SimpleGrantedAuthority(permission.getCode()));
            }
        }

        authUser.setRoles(roles);
        return new CustomUserDetails(authUser);
    }
}
