package dev.pabloabad.adventuretime.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    
    private Long id;
    private String title;
    private String description;
    private Optional<String> imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime eventDateTime;
    private int maxParticipants;
    private Boolean isAvailable;
    private Boolean isFeatured;
    private int participantsCount;
    private List<Long> registered;
}
