package uz.pdp.maven.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.pdp.maven.model.Todo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class TodoRepo {

    private NamedParameterJdbcTemplate template;
    private RowMapper<Todo> rowMapper = (rs, rowNum) -> {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        int ownerId = rs.getInt("owner_id");
        LocalDateTime deadline = rs.getTimestamp("deadline").toLocalDateTime();

        return Todo.builder()
                .id(id)
                .name(name)
                .description(description)
                .ownerId(ownerId)
                .deadline(deadline)
                .build();
    };

    public TodoRepo(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public Todo get(int id) {
        String sql = "select * from todos where id = :id";
        Map<String, Object> paramSource = Map.of("id", id);

        Todo todo = template.queryForObject(sql, paramSource, rowMapper);
        return todo;
    }

    public void delete(int id) {
        String sql = "delete from todos where id = :id";
        template.update(sql, Map.of("id", id));
    }

    public void save(Todo todo) {
        if (todo == null) {
            throw new IllegalArgumentException("Todo must not be null");
        }
        if (todo.getName() == null || todo.getDescription() == null || todo.getDeadline() == null) {
            throw new IllegalArgumentException("Todo fields must not be null");
        }

        String sql = "insert into todos (name, description, deadline, owner_id) " +
                "values (:name, :description, :deadline, :ownerId)";
        Map<String, Object> params = Map.of(
                "name", todo.getName(),
                "description", todo.getDescription(),
                "deadline", Timestamp.valueOf(todo.getDeadline()),
                "ownerId", todo.getOwnerId()
        );

        template.update(sql, params);
    }

    public List<Todo> getAll() {
        String sql = "select * from todos";
        List<Todo> todos = template.query(sql, rowMapper);

        return todos;
    }

    public List<Todo> getAllByOwnerId(int ownerId) {
        String sql = "select * from todos where owner_id = :ownerId";
        List<Todo> todos = template.query(sql, Map.of("ownerId", ownerId), rowMapper);

        return todos;
    }

    public void update(Todo todo) {
        String sql = "UPDATE todos SET name = :name, " +
                "description = :description, " +
                "deadline = :deadline, " +
                "owner_id = :ownerId " +
                "WHERE id = :id";

        Map<String, Object> parameters = Map.of(
                "name", todo.getName(),
                "description", todo.getDescription(),
                "deadline", Timestamp.valueOf(todo.getDeadline()),
                "ownerId", todo.getOwnerId(),
                "id", todo.getId()
        );

        template.update(sql, parameters);
    }

    public List<Todo> findByName(String name) {
        String sql = "SELECT * FROM todos WHERE name ILIKE :name";

        List<Todo> todos = template.query(sql, Map.of("name", "%" + name + "%"), rowMapper);

        return todos;
    }

}
