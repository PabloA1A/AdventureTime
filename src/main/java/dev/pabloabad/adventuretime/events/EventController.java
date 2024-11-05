package dev.pabloabad.adventuretime.events;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pabloabad.adventuretime.dtos.EventDto;
import dev.pabloabad.adventuretime.dtos.NewEventDto;
import dev.pabloabad.adventuretime.events.exceptions.EventException;
import dev.pabloabad.adventuretime.events.exceptions.EventNotFoundException;

@RestController
@RequestMapping(path = "${api-endpoint}/event")
public class EventController {
    
    @Autowired
    private EventService eventService;

    @GetMapping("/allhome")
    public ResponseEntity<Page<EventDto>> getAllEvents(
            @PageableDefault(size = 10, sort = "eventDateTime") Pageable pageable) {
        Page<EventDto> events = eventService.getAllEventsHome(pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<EventDto> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<EventDto>> getFeaturedEvents() {
        List<EventDto> featuredEvents = eventService.getFeaturedEvents();
        return ResponseEntity.ok(featuredEvents);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addEvent(@RequestBody NewEventDto newEventDto) {
        try {
            String imageUrl = null;
            if (newEventDto.getImageHash() != null && newEventDto.getImageHash().isPresent()) {
           /*      imageUrl = imageService.uploadBase64(newEventDto.getImageHash().get()).orElse(null); */
            }
            EventDto eventDto = new EventDto(newEventDto.getId(), newEventDto.getTitle(), newEventDto.getDescription(),
                    Optional.ofNullable(imageUrl), newEventDto.getEventDateTime(), newEventDto.getMaxParticipants(),
                    newEventDto.getIsAvailable(), newEventDto.getIsFeatured(), 0, null);
            Event addEvent = eventService.addEvent(eventDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Event added successfully with id: " + addEvent.getId());
        } catch (EventException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid event data: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEvent(@PathVariable Long id, @RequestBody NewEventDto editEventDto) {
        try {
            Optional<String> imageUrl = Optional.empty();
            if (editEventDto.getImageHash().isPresent()) {
            /*     imageUrl = Optional.of(imageService.uploadBase64(editEventDto.getImageHash().get()).get()); */
            }
            EventDto eventDto = new EventDto(editEventDto.getId(), editEventDto.getTitle(),
                    editEventDto.getDescription(),
                    imageUrl, editEventDto.getEventDateTime(), editEventDto.getMaxParticipants(),
                    editEventDto.getIsAvailable(), editEventDto.getIsFeatured(), 0, null);
            eventService.updateEvent(id, eventDto);
            return ResponseEntity.ok("Event updated successfully");
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found: " + e.getMessage());
        } catch (EventException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid event data: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found: " + e.getMessage());
        } catch (EventException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the event: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<EventDto>> getEventById(@PathVariable Long id) {
        Optional<EventDto> event = eventService.getEventById(id);
        if (!event.isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body(event);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(event);
    }
}
