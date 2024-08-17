package uz.pdp.maven.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.pdp.maven.model.AuthUser;
import uz.pdp.maven.model.Role;

import java.util.*;

@Repository
public class RoleRepo {

    private NamedParameterJdbcTemplate template;
    private RowMapper<Role> rowMapper = (rs, rowNum) -> {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String code = rs.getString("code");

        return Role.builder()
                .id(id)
                .name(name)
                .code(code).build();
    };

    public RoleRepo(NamedParameterJdbcTemplate template) {
        this.template = template;
    }


    public List<Role> getByUserId(Integer userId) {
        String sql = """
                select r.* from role_user ru 
                inner join role r 
                on r.id = ru.role_id
                where ru.user_id = :userId
                """;
        try {
            return template.query(sql, Map.of("userId", userId), rowMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
