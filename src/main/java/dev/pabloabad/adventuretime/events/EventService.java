package dev.pabloabad.adventuretime.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.pabloabad.adventuretime.events.exceptions.EventNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Page<EventDto> getAllEventsHome(Pageable pageable) {
        return eventRepository.findAll(pageable)
                .map(this::convertToDtoHome);
    }

    private EventDto convertToDtoHome(Event event) {
        List<Long> subscribeDto = event.getParticipants().stream()
                .map(participant -> {
                    User user = participant.getUser();
                    return user != null ? user.getId() : null;
                })
                .collect(Collectors.toList());

        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                Optional.of(event.getImageUrl()),
                event.getEventDateTime(),
                event.getMaxParticipants(),
                event.getIsAvailable(),
                event.getIsFeatured(),
                event.getParticipants().size(),
                subscribeDto);
    }

    private EventDto convertToDto(Event event) {

        List<Long> SubscribeDto = event.getParticipants().stream()
                .map(participant -> {
                    User user = participant.getUser();
                    return user != null ? user.getId() : null;
                })
                .collect(Collectors.toList());

        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                Optional.of(event.getImageUrl()),
                event.getEventDateTime(),
                event.getMaxParticipants(),
                event.getIsAvailable(),
                event.getIsFeatured(),
                event.getParticipants().size(),
                SubscribeDto);
    }

    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EventDto> getFeaturedEvents() {
        return eventRepository.findByIsFeaturedTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<EventDto> getEventById(Long id) {

        return eventRepository.findById(id).map(this::convertToDto);
    }

    public Event addEvent(EventDto eventDto) {
        if (eventDto.getTitle() == null || eventDto.getTitle().isEmpty()) {
            throw new EventException("Event title cannot be null or empty");
        }

        Event newEvent = new Event();
        newEvent.setTitle(eventDto.getTitle());
        newEvent.setDescription(eventDto.getDescription());
        newEvent.setImageUrl(eventDto.getImageUrl().get());
        newEvent.setEventDateTime(eventDto.getEventDateTime());
        newEvent.setMaxParticipants(eventDto.getMaxParticipants());
        newEvent.setIsFeatured(eventDto.getIsFeatured());
        newEvent.setIsAvailable(eventDto.getIsAvailable());

        return eventRepository.save(newEvent);
    }

    public Event updateEvent(Long id, EventDto eventDto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + id + " not found"));

        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        if (eventDto.getImageUrl().isPresent())
            event.setImageUrl(eventDto.getImageUrl().get());
        event.setEventDateTime(eventDto.getEventDateTime());
        event.setMaxParticipants(eventDto.getMaxParticipants());
        event.setIsFeatured(eventDto.getIsFeatured());
        event.setIsAvailable(eventDto.getIsAvailable());

        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + id + " not found"));

        eventRepository.delete(event);
    }
}
