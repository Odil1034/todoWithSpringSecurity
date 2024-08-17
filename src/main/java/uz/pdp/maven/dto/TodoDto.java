package uz.pdp.maven.dto;

import java.time.LocalDateTime;

public record TodoDto(
        String name,
        String description,
        LocalDateTime deadline) {
}
