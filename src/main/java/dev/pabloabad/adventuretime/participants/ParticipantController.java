package dev.pabloabad.adventuretime.participants;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.pabloabad.adventuretime.dtos.EventDto;
import dev.pabloabad.adventuretime.dtos.ParticipantDto;
import dev.pabloabad.adventuretime.events.EventService;
import dev.pabloabad.adventuretime.participants.exceptions.ParticipantNotFoundException;

@RestController
@RequestMapping(path = "${api-endpoint}/participant")
public class ParticipantController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ParticipantService participantService;

    @GetMapping("/all")
    public ResponseEntity<List<ParticipantDto>> getAllParticipants() {
        try {
            List<ParticipantDto> participants = participantService.getAllParticipants();
            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{eventId}/{id}")
    public ResponseEntity<ParticipantDto> getParticipantById(@PathVariable Long id) {
        try {
            ParticipantDto participantDto = participantService.getParticipantById(id);
            return ResponseEntity.ok(participantDto);
        } catch (ParticipantNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{eventId}/{participantId}")
    public ResponseEntity<String> deleteParticipant(@PathVariable Long eventId, @PathVariable Long participantId) {
        try {
            participantService.deleteParticipant(eventId, participantId);
            return ResponseEntity.status(HttpStatus.OK).body("Participant deleted successfully");
        } catch (ParticipantNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Participant not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the participant: " + e.getMessage());
        }
    }

    @DeleteMapping("/{eventId}/unregister/{userId}")
    public ResponseEntity<String> unregisterFromEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        try {
            participantService.unregisterFromEvent(eventId, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully unregistered from the event");
        } catch (ParticipantNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Participant not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while unregistering from the event: " + e.getMessage());
        }
    }

    @PostMapping("/{eventId}/join/{userId}")
    public ResponseEntity<String> joinEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        try {
            Optional<EventDto> eventOptional = eventService.getEventById(eventId);
            if (!eventOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            EventDto event = eventOptional.get();
            if (event.getParticipantsCount() >= event.getMaxParticipants()) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Event is full");
            }

            boolean joined = participantService.joinEvent(eventId, userId);
            if (!joined) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body("User is already registered for the event");
            }

            return ResponseEntity.ok("Successfully joined the event");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while joining the event: " + e.getMessage());
        }
    }
}
