package dev.pabloabad.adventuretime.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import dev.pabloabad.adventuretime.dtos.EventDto;
import dev.pabloabad.adventuretime.events.exceptions.EventException;
import dev.pabloabad.adventuretime.events.exceptions.EventNotFoundException;
import dev.pabloabad.adventuretime.participants.Participant;
import dev.pabloabad.adventuretime.users.User;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEventByIdNotFound() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<EventDto> result = eventService.getEventById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testAddEventWithNullTitle() {
        EventDto eventDto = new EventDto();
        eventDto.setTitle(null);

        Exception exception = assertThrows(EventException.class, () -> {
            eventService.addEvent(eventDto);
        });
        assertEquals("Event title cannot be null or empty", exception.getMessage());
    }

    @Test
    void testUpdateEventNotFound() {
        EventDto eventDto = new EventDto();
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EventNotFoundException.class, () -> {
            eventService.updateEvent(1L, eventDto);
        });
        assertEquals("Event with id 1 not found", exception.getMessage());
    }

    @Test
    void testDeleteEvent() {
        Event event = new Event();
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        eventService.deleteEvent(1L);
        verify(eventRepository, times(1)).delete(event);
    }

    @Test
    void testDeleteEventNotFound() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EventNotFoundException.class, () -> {
            eventService.deleteEvent(1L);
        });
        assertEquals("Event with id 1 not found", exception.getMessage());
    }

    @Test
    void testGetAllEventsHome() {
        Pageable pageable = PageRequest.of(0, 10);
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");
        event1.setDescription("Description 1");
        event1.setImageUrl("image1.jpg");
        event1.setEventDateTime(LocalDateTime.now());
        event1.setMaxParticipants(10);
        event1.setIsAvailable(true);
        event1.setIsFeatured(false);
        event1.setParticipants(new ArrayList<>());

        Page<Event> eventPage = new PageImpl<>(List.of(event1));
        when(eventRepository.findAll(pageable)).thenReturn(eventPage);

        Page<EventDto> result = eventService.getAllEventsHome(pageable);

        assertEquals(1, result.getTotalElements());
        EventDto eventDto = result.getContent().get(0);
        assertEquals(event1.getId(), eventDto.getId());
        assertEquals(event1.getTitle(), eventDto.getTitle());
        assertEquals(event1.getDescription(), eventDto.getDescription());
        assertEquals(event1.getImageUrl(), eventDto.getImageUrl().get());
        verify(eventRepository).findAll(pageable);
    }

    @Test
    void testGetEventById_WithParticipants() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setImageUrl("test.jpg");
        event.setEventDateTime(LocalDateTime.now());
        event.setMaxParticipants(10);
        event.setIsAvailable(true);
        event.setIsFeatured(false);

        User user = new User();
        user.setId(1L);

        Participant participant = new Participant();
        participant.setUser(user);

        event.setParticipants(List.of(participant));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<EventDto> result = eventService.getEventById(1L);

        assertTrue(result.isPresent());
        EventDto eventDto = result.get();
        assertEquals(event.getId(), eventDto.getId());
        assertEquals(event.getTitle(), eventDto.getTitle());
        assertEquals(event.getDescription(), eventDto.getDescription());
        assertEquals(event.getImageUrl(), eventDto.getImageUrl().get());
        assertEquals(1, eventDto.getRegistered().size());
        assertEquals(user.getId(), eventDto.getRegistered().get(0));
    }

    @Test
    void testGetEventById_WithNullUser() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setImageUrl("test.jpg");
        event.setEventDateTime(LocalDateTime.now());
        event.setMaxParticipants(10);
        event.setIsAvailable(true);
        event.setIsFeatured(false);

        Participant participant = new Participant();
        participant.setUser(null);

        event.setParticipants(List.of(participant));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<EventDto> result = eventService.getEventById(1L);

        assertTrue(result.isPresent());
        EventDto eventDto = result.get();
        assertEquals(event.getId(), eventDto.getId());
        assertEquals(1, eventDto.getRegistered().size());
        assertNull(eventDto.getRegistered().get(0));
    }

    @Test
    void testGetAllEvents() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");
        event1.setDescription("Description 1");
        event1.setImageUrl("image1.jpg");
        event1.setEventDateTime(LocalDateTime.now());
        event1.setMaxParticipants(10);
        event1.setIsAvailable(true);
        event1.setIsFeatured(false);
        event1.setParticipants(new ArrayList<>());

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Event 2");
        event2.setDescription("Description 2");
        event2.setImageUrl("image2.jpg");
        event2.setEventDateTime(LocalDateTime.now());
        event2.setMaxParticipants(10);
        event2.setIsAvailable(true);
        event2.setIsFeatured(false);
        event2.setParticipants(new ArrayList<>());

        when(eventRepository.findAll()).thenReturn(List.of(event1, event2));

        List<EventDto> result = eventService.getAllEvents();

        assertEquals(2, result.size());
        verify(eventRepository).findAll();

        assertEquals(event1.getId(), result.get(0).getId());
        assertEquals(event1.getTitle(), result.get(0).getTitle());
        assertEquals(event1.getImageUrl(), result.get(0).getImageUrl().get());

        assertEquals(event2.getId(), result.get(1).getId());
        assertEquals(event2.getTitle(), result.get(1).getTitle());
        assertEquals(event2.getImageUrl(), result.get(1).getImageUrl().get());
    }

    @Test
    void testGetFeaturedEvents() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Featured Event");
        event1.setDescription("Description");
        event1.setImageUrl("image1.jpg");
        event1.setEventDateTime(LocalDateTime.now());
        event1.setMaxParticipants(10);
        event1.setIsAvailable(true);
        event1.setIsFeatured(true);
        event1.setParticipants(new ArrayList<>());

        when(eventRepository.findByIsFeaturedTrue()).thenReturn(List.of(event1));

        List<EventDto> result = eventService.getFeaturedEvents();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsFeatured());
        assertEquals("image1.jpg", result.get(0).getImageUrl().get());
        assertEquals("Featured Event", result.get(0).getTitle());
        verify(eventRepository).findByIsFeaturedTrue();
    }

    @Test
    void testAddEvent_Success() {
        EventDto eventDto = new EventDto();
        eventDto.setTitle("Test Event");
        eventDto.setDescription("Test Description");
        eventDto.setImageUrl(Optional.of("test.jpg"));
        eventDto.setEventDateTime(LocalDateTime.now());
        eventDto.setMaxParticipants(10);
        eventDto.setIsFeatured(false);
        eventDto.setIsAvailable(true);

        Event savedEvent = new Event();
        savedEvent.setId(1L);
        savedEvent.setTitle(eventDto.getTitle());
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        Event result = eventService.addEvent(eventDto);

        assertNotNull(result);
        assertEquals(eventDto.getTitle(), result.getTitle());
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void testAddEvent_NullTitle() {
        EventDto eventDto = new EventDto();
        eventDto.setTitle(null);

        EventException exception = assertThrows(
                EventException.class,
                () -> eventService.addEvent(eventDto));
        assertEquals("Event title cannot be null or empty", exception.getMessage());
    }

    @Test
    void testAddEvent_EmptyTitle() {
        EventDto eventDto = new EventDto();
        eventDto.setTitle("");

        EventException exception = assertThrows(
                EventException.class,
                () -> eventService.addEvent(eventDto));
        assertEquals("Event title cannot be null or empty", exception.getMessage());
    }

    @Test
    void testUpdateEvent_Success() {
        Long eventId = 1L;
        EventDto eventDto = new EventDto();
        eventDto.setTitle("Updated Event");
        eventDto.setDescription("Updated Description");
        eventDto.setImageUrl(Optional.of("updated.jpg"));
        eventDto.setEventDateTime(LocalDateTime.now());
        eventDto.setMaxParticipants(20);
        eventDto.setIsFeatured(true);
        eventDto.setIsAvailable(true);

        Event existingEvent = new Event();
        existingEvent.setId(eventId);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(existingEvent);

        Event result = eventService.updateEvent(eventId, eventDto);

        assertNotNull(result);
        assertEquals(eventDto.getTitle(), result.getTitle());
        assertEquals(eventDto.getDescription(), result.getDescription());
        assertEquals(eventDto.getImageUrl().get(), result.getImageUrl());
        verify(eventRepository).findById(eventId);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void testUpdateEvent_NotFound() {
        Long eventId = 1L;
        EventDto eventDto = new EventDto();
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        EventNotFoundException exception = assertThrows(
                EventNotFoundException.class,
                () -> eventService.updateEvent(eventId, eventDto));
        assertEquals("Event with id 1 not found", exception.getMessage());
    }

    @Test
    void testGetParticipantWithNullUser() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setImageUrl("test.jpg");
        event.setEventDateTime(LocalDateTime.now());
        event.setMaxParticipants(10);
        event.setIsAvailable(true);
        event.setIsFeatured(false);

        Participant participant = new Participant();
        participant.setUser(null);
        event.setParticipants(List.of(participant));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<EventDto> result = eventService.getEventById(1L);

        assertTrue(result.isPresent());
        EventDto eventDto = result.get();
        assertEquals(1, eventDto.getRegistered().size());
        assertNull(eventDto.getRegistered().get(0), "Debería ser null cuando el usuario es null");
    }

    @Test
    void testGetParticipantWithValidUser() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setImageUrl("test.jpg");
        event.setEventDateTime(LocalDateTime.now());
        event.setMaxParticipants(10);
        event.setIsAvailable(true);
        event.setIsFeatured(false);

        User user = new User();
        user.setId(1L);

        Participant participant = new Participant();
        participant.setUser(user);
        event.setParticipants(List.of(participant));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<EventDto> result = eventService.getEventById(1L);

        assertTrue(result.isPresent());
        EventDto eventDto = result.get();
        assertEquals(1, eventDto.getRegistered().size());
        assertEquals(1L, eventDto.getRegistered().get(0), "Debería ser el ID del usuario cuando el usuario existe");
    }

    @Test
    void testParticipantWithNullUser() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setImageUrl("test.jpg");
        event.setEventDateTime(LocalDateTime.now());
        event.setMaxParticipants(10);
        event.setIsAvailable(true);
        event.setIsFeatured(false);

        Participant participant = new Participant();
        participant.setUser(null);
        event.setParticipants(List.of(participant));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<EventDto> result = eventService.getEventById(1L);

        assertTrue(result.isPresent());
        EventDto eventDto = result.get();
        assertEquals(1, eventDto.getRegistered().size());
        assertNull(eventDto.getRegistered().get(0), "Debe ser null cuando el usuario es null");
    }

    @Test
    void testParticipantWithValidUser() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setImageUrl("test.jpg");
        event.setEventDateTime(LocalDateTime.now());
        event.setMaxParticipants(10);
        event.setIsAvailable(true);
        event.setIsFeatured(false);

        User user = new User();
        user.setId(1L);

        Participant participant = new Participant();
        participant.setUser(user);
        event.setParticipants(List.of(participant));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<EventDto> result = eventService.getEventById(1L);

        assertTrue(result.isPresent());
        EventDto eventDto = result.get();
        assertEquals(1, eventDto.getRegistered().size());
        assertEquals(1L, eventDto.getRegistered().get(0), "Debe ser el ID del usuario cuando existe");
    }
}
