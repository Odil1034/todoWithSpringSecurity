package uz.pdp.maven.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.pdp.maven.model.Permission;

import java.util.List;
import java.util.Map;

@Repository
public class PermissionRepo {

    private NamedParameterJdbcTemplate template;

    private RowMapper<Permission> rowMapper = ((rs, rowNum) -> {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String code = rs.getString("code");

        return Permission.builder()
                .id(id)
                .name(name)
                .code(code)
                .build();
    });

    public PermissionRepo(NamedParameterJdbcTemplate template) {
        this.template = template;
    }


    public List<Permission> getByRoleId(Integer roleId) {
        String sql = """
                select p.* from role_permission rp
                inner join permission p on p.id = rp.permission_id
                where rp.role_id = :roleId
                """;

        List<Permission> permissions = template.query(sql, Map.of("roleId", roleId), rowMapper);
        return permissions;
    }
}
