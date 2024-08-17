package uz.pdp.maven.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUser {

    private Integer id;
    private String name;
    private String username;
    private String password;
    private UserStatus status;
    private boolean isDeleted;
    private List<Role> roles;
    private int uploadId;

}
