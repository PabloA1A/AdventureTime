package dev.pabloabad.adventuretime.participants;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    
    Optional<Participant> findByEventIdAndUserId(Long eventId, Long userId);
}
