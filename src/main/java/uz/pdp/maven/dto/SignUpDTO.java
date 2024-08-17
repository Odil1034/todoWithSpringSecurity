package uz.pdp.maven.dto;

import org.springframework.web.multipart.MultipartFile;

public record SignUpDTO(
        String name,
        String username,
        String password,
        MultipartFile file) {

}
