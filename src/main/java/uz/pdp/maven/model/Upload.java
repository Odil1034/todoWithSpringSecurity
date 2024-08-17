package uz.pdp.maven.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Upload {

    private int id;
    private String originalName;
    private String generatedName;
    private String extension;
    private Long size;

}
