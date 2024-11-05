package dev.pabloabad.adventuretime.dtos;

import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    
    private Long id;
    private String title;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Optional<String> imageHash;
    private LocalDateTime eventDateTime;
    private int maxParticipants;
    private Boolean isAvailable;
    private Boolean isFeatured;
}
