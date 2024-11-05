package dev.pabloabad.adventuretime.participants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.pabloabad.adventuretime.dtos.EventDto;
import dev.pabloabad.adventuretime.dtos.ParticipantDto;
import dev.pabloabad.adventuretime.events.EventService;
import dev.pabloabad.adventuretime.participants.exceptions.ParticipantNotFoundException;
import dev.pabloabad.adventuretime.profiles.ProfileService;

public class ParticipantControllerTest {

    @Mock
    private ParticipantService participantService;

    @Mock
    private EventService eventService;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ParticipantController participantController;

    private Long eventId;
    @SuppressWarnings("unused")
    private Long userId;
    private EventDto eventDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        eventId = 1L;
        userId = 1L;
        eventDto = new EventDto(eventId, null, null, null, null, 0, null, null, 0, null);
        eventDto.setId(eventId);
        eventDto.setTitle("Test Event");
        eventDto.setMaxParticipants(10);
        eventDto.setParticipantsCount(5);
    }

    @Test
    void getAllParticipants_Success() {
        List<ParticipantDto> participants = Arrays.asList(new ParticipantDto(null, null, null, null),
                new ParticipantDto(null, null, null, null));
        when(participantService.getAllParticipants()).thenReturn(participants);

        ResponseEntity<List<ParticipantDto>> response = participantController.getAllParticipants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participants, response.getBody());
    }

    @Test
    void getAllParticipants_Exception() {
        when(participantService.getAllParticipants()).thenThrow(new RuntimeException());

        ResponseEntity<List<ParticipantDto>> response = participantController.getAllParticipants();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getParticipantById_Success() throws ParticipantNotFoundException {
        Long id = 1L;
        ParticipantDto participantDto = new ParticipantDto(id, null, id, id);
        when(participantService.getParticipantById(id)).thenReturn(participantDto);

        ResponseEntity<ParticipantDto> response = participantController.getParticipantById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participantDto, response.getBody());
    }

    @Test
    void getParticipantById_NotFound() throws ParticipantNotFoundException {
        Long id = 1L;
        when(participantService.getParticipantById(id)).thenThrow(new ParticipantNotFoundException("Not found"));

        ResponseEntity<ParticipantDto> response = participantController.getParticipantById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void joinEvent_EventFull() {
        Long eventId = 1L;
        Long userId = 1L;
        when(participantService.joinEvent(eventId, userId)).thenReturn(false);
        EventDto eventDto = new EventDto(userId, null, null, null, null, 0, null, null, 0, null);
        eventDto.setMaxParticipants(10);
        eventDto.setParticipantsCount(10);
        when(eventService.getEventById(eventId)).thenReturn(Optional.of(eventDto));

        ResponseEntity<String> response = participantController.joinEvent(eventId, userId);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals("Event is full", response.getBody());
    }

    @Test
    void joinEvent_AlreadyRegistered() {
        Long eventId = 1L;
        Long userId = 1L;
        when(participantService.joinEvent(eventId, userId)).thenReturn(false);
        EventDto eventDto = new EventDto(userId, null, null, null, null, 0, null, null, 0, null);
        eventDto.setMaxParticipants(10);
        eventDto.setParticipantsCount(5);
        when(eventService.getEventById(eventId)).thenReturn(Optional.of(eventDto));

        ResponseEntity<String> response = participantController.joinEvent(eventId, userId);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals("User is already registered for the event", response.getBody());
    }

    @Test
    void deleteParticipant_Success() throws ParticipantNotFoundException {
        Long eventId = 1L;
        Long participantId = 1L;
        doNothing().when(participantService).deleteParticipant(eventId, participantId);

        ResponseEntity<String> response = participantController.deleteParticipant(eventId, participantId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Participant deleted successfully", response.getBody());
    }

    @Test
    void deleteParticipant_NotFound() throws ParticipantNotFoundException {
        Long eventId = 1L;
        Long participantId = 1L;
        doThrow(new ParticipantNotFoundException("Not found")).when(participantService).deleteParticipant(eventId,
                participantId);

        ResponseEntity<String> response = participantController.deleteParticipant(eventId, participantId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Participant not found: Not found", response.getBody());
    }

    @Test
    void unregisterFromEvent_Success() throws ParticipantNotFoundException {
        Long eventId = 1L;
        Long userId = 1L;
        doNothing().when(participantService).unregisterFromEvent(eventId, userId);

        ResponseEntity<String> response = participantController.unregisterFromEvent(eventId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully unregistered from the event", response.getBody());
    }

    @Test
    void unregisterFromEvent_NotFound() throws ParticipantNotFoundException {
        Long eventId = 1L;
        Long userId = 1L;
        doThrow(new ParticipantNotFoundException("Not found")).when(participantService).unregisterFromEvent(eventId,
                userId);

        ResponseEntity<String> response = participantController.unregisterFromEvent(eventId, userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Participant not found: Not found", response.getBody());
    }

    @Test
    void joinEvent_EventNotFound() {
        Long eventId = 1L;
        Long userId = 1L;
        when(eventService.getEventById(eventId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = participantController.joinEvent(eventId, userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void joinEvent_Success() {
        Long eventId = 1L;
        Long userId = 1L;
        EventDto eventDto = new EventDto(userId, null, null, null, null, 0, null, null, 5, null);
        eventDto.setMaxParticipants(10);
        when(eventService.getEventById(eventId)).thenReturn(Optional.of(eventDto));
        when(participantService.joinEvent(eventId, userId)).thenReturn(true);

        ResponseEntity<String> response = participantController.joinEvent(eventId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully joined the event", response.getBody());
    }

    @Test
    void joinEvent_InternalServerError() {
        Long eventId = 1L;
        Long userId = 1L;
        EventDto eventDto = new EventDto(userId, null, null, null, null, 0, null, null, 5, null);
        eventDto.setMaxParticipants(10);
        when(eventService.getEventById(eventId)).thenReturn(Optional.of(eventDto));
        when(participantService.joinEvent(eventId, userId)).thenThrow(new RuntimeException("Test error"));

        ResponseEntity<String> response = participantController.joinEvent(eventId, userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while joining the event: Test error", response.getBody());
    }

    @Test
    void deleteParticipant_InternalServerError() {
        Long eventId = 1L;
        Long participantId = 1L;
        doThrow(new RuntimeException("Test error")).when(participantService).deleteParticipant(eventId, participantId);

        ResponseEntity<String> response = participantController.deleteParticipant(eventId, participantId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while deleting the participant: Test error", response.getBody());
    }

    @Test
    void unregisterFromEvent_InternalServerError() {
        Long eventId = 1L;
        Long userId = 1L;
        doThrow(new RuntimeException("Test error")).when(participantService).unregisterFromEvent(eventId, userId);

        ResponseEntity<String> response = participantController.unregisterFromEvent(eventId, userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while unregistering from the event: Test error", response.getBody());
    }
}
