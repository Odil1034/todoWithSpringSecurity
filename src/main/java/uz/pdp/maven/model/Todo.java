package uz.pdp.maven.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Todo {

    private Integer id;
    private String name;
    private String description;
    private LocalDateTime deadline;
    private int ownerId;

}
