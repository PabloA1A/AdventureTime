package dev.pabloabad.adventuretime.events;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.pabloabad.adventuretime.participants.Participant;

public interface EventRepository extends JpaRepository<Event, Long>  {
    
    List<Event> findByIsFeaturedTrue();

    @Query("SELECT p FROM Event e JOIN e.participants p WHERE e.id = :eventId")
    List<Participant> getParticipantsByEventId(@Param("eventId") Long eventId);
}
