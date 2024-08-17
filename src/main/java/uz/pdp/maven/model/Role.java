package uz.pdp.maven.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {

    private Integer id;
    private String name;
    private String code;
    private List<Permission> permissions;

}
