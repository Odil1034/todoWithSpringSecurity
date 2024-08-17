package uz.pdp.maven.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import uz.pdp.maven.model.Upload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class UploadRepo {

    private final NamedParameterJdbcTemplate template;
    private final RowMapper<Upload> rowMapper = (rs, rowNum) -> {
        int id = rs.getInt("id");
        String originalName = rs.getString("original_name");
        String generatedName = rs.getString("generated_name");
        String extension = rs.getString("extension");
        Long size = rs.getLong("size");
        return Upload.builder()
                .id(id)
                .originalName(originalName)
                .generatedName(generatedName)
                .extension(extension)
                .size(size)
                .build();
    };

    public UploadRepo(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public Upload get(int uploadId) {

        String sql = "select from uploads where id = :id";
        Map<String, Object> paramSource = new HashMap<>();
        paramSource.put("id", uploadId);

        return template.queryForObject(sql, paramSource, rowMapper);
    }

    public int save(Upload upload) {
        String sql = "insert into uploads (original_name, generated_name, extension, size) " +
                "values (:originalName, :generatedName, :extension, :size)";

        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(
                "originalName", upload.getOriginalName(),
                "generatedName", upload.getGeneratedName(),
                "extension", upload.getExtension(),
                "size", upload.getSize()
        ));

        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(sql, parameters, keyHolder, new String[]{"id"});

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public List<Upload> getAll() {
        String sql = "select * from uploads";
        List<Upload> uploads = template.query(sql, rowMapper);
        return uploads;
    }

    public Upload getByUserId(int userId) {
        String sql = "SELECT u.* FROM user_uploads uu " +
                "INNER JOIN uploads u ON u.id = uu.upload_id " +
                "WHERE uu.user_id = :userId";
        try {
            return template.queryForObject(sql, Map.of("userId", userId), rowMapper);
        } catch (Exception e) {
            return null;
        }
    }
}
