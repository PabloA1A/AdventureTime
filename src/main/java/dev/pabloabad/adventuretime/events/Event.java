package dev.pabloabad.adventuretime.events;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import dev.pabloabad.adventuretime.participants.Participant;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    public Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "event_datetime", nullable = false)
    private LocalDateTime eventDateTime;

    @Column(name = "max_participants", nullable = false)
    private int maxParticipants;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured;

    @Transient
    private int participantsCount;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Participant> participants;

    public int getParticipantsCount() {
        return (participants != null) ? participants.size() : 0;
    }
}
