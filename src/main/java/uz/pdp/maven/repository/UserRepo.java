package uz.pdp.maven.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import uz.pdp.maven.model.AuthUser;
import uz.pdp.maven.model.Role;
import uz.pdp.maven.model.UserStatus;

import java.util.*;

@Repository
public class UserRepo {

    private RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final NamedParameterJdbcTemplate template;


    public UserRepo(RoleRepo roleRepo, NamedParameterJdbcTemplate template, PasswordEncoder passwordEncoder) {
        this.roleRepo = roleRepo;
        this.template = template;
        this.passwordEncoder = passwordEncoder;
    }

    private final RowMapper<AuthUser> rowMapper = (rs, rowNum) -> {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String name = rs.getString("name");
        String status = rs.getString("status");
        int uploadId = rs.getInt("upload_id");
        boolean isDelete = rs.getBoolean("is_deleted");
        List<Role> roles = roleRepo.getByUserId(id);
        return AuthUser.builder()
                .id(id)
                .username(username)
                .name(name)
                .password(password)
                .status(UserStatus.valueOf(status))
                .isDeleted(isDelete)
                .uploadId(uploadId)
                .roles(roles)
                .build();
    };

    public Optional<AuthUser> get(int id) {

        String sql = "select * from users where id = :id";
        Map<String, Object> paramSource = new HashMap<>();
        paramSource.put("id", id);

        AuthUser authUser = template.queryForObject(sql, paramSource, rowMapper);
        if (authUser != null) {
            return Optional.of(authUser);
        } else {
            return Optional.empty();
        }
    }

    public void save(AuthUser authUser) {
        String sql = "insert into users (username, password, name, status, is_deleted, upload_id) " +
                "values (:username, :password, :name, :status, :isDeleted, :uploadId)";
        String password = authUser.getPassword();
        String encode = passwordEncoder.encode(password);
        Map<String, Object> name = Map.of(
                "username", authUser.getUsername(),
                "password", encode,
                "name", authUser.getName(),
                "uploadId", authUser.getUploadId(),
                "isDeleted", false,
                "status", UserStatus.ACTIVE.toString());

        template.update(sql, name);
    }

    public List<AuthUser> getAll() {
        String sql = "select * from users";
        List<AuthUser> authUsers = template.query(sql, rowMapper);
        return authUsers;
    }

    public Optional<AuthUser> getByUsername(String username) {
        String sql = "select * from users where username = :username";
        try {
            AuthUser authUser = template.queryForObject(sql, Map.of("username", username), rowMapper);
            return Optional.ofNullable(authUser);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void blockUser(int userId) {
        String sql = "UPDATE users SET status = :status WHERE id = :userId";
        template.update(sql, Map.of("status", UserStatus.BLOCKED.toString(), "userId", userId));
    }

    public void deleteUser(int userId) {
        String sql = "update users set is_deleted = :delete where id = userId";
        template.update(sql, Map.of("delete", true, "userId", userId));
    }
}
