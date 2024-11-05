package dev.pabloabad.adventuretime.participants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.pabloabad.adventuretime.users.SecurityUser;
import dev.pabloabad.adventuretime.users.User;

@Service
public class ParticipantService {
    
    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public ParticipantDto addParticipant(ParticipantDto participantDto) {
        Participant participant = new Participant();
        participant.setJoinedAt(LocalDateTime.now());

        Optional<Event> event = eventRepository.findById(participantDto.getEventId());
        event.ifPresent(participant::setEvent);

        Optional<User> user = userRepository.findById(participantDto.getUserId());
        user.ifPresent(participant::setUser);

        Participant savedParticipant = participantRepository.save(participant);

        return convertToDto(savedParticipant);
    }

    public List<ParticipantDto> getAllParticipants() {
        List<Participant> participants = participantRepository.findAll();
        return participants.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ParticipantDto getParticipantById(Long id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ParticipantNotFoundException("Participant not found with ID: " + id));
        return convertToDto(participant);
    }

    public boolean joinEvent(Long eventId, Long userId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            if (event.getParticipantsCount() >= event.getMaxParticipants()) {
                return false;
            }

            boolean isAlreadyRegistered = participantRepository.findByEventIdAndUserId(eventId, userId).isPresent();
            if (isAlreadyRegistered) {
                return false;
            }

            Participant participant = new Participant();
            participant.setEvent(eventOptional.get());
            participant.setUser(userOptional.get());
            participant.setJoinedAt(LocalDateTime.now());

            participantRepository.save(participant);
            return true;
        }

        return false;
    }

    public void deleteParticipant(Long eventId, Long participantId) {

        if (!eventRepository.existsById(eventId)) {
            throw new ParticipantNotFoundException("Event not found with ID: " + eventId);
        }

        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ParticipantNotFoundException("Participant not found with ID: " + participantId));

        if (!participant.getEvent().getId().equals(eventId)) {
            throw new ParticipantNotFoundException(
                    "Participant with ID: " + participantId + " is not associated with event ID: " + eventId);
        }
        participantRepository.delete(participant);
    }

    public void unregisterFromEvent(Long eventId, Long userId) {

        Long authenticatedUserId = getAuthenticatedUserId();

        if (!authenticatedUserId.equals(userId)) {
            throw new SecurityException("You can only unregister yourself from the event.");
        }

        Participant participant = participantRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new ParticipantNotFoundException(
                        "Participant not found for the event with ID: " + eventId + " and user ID: " + userId));

        participantRepository.delete(participant);
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        if (authentication.getPrincipal() instanceof SecurityUser) {

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            return user.getId();
        } else if (authentication.getPrincipal() instanceof String) {

            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();
        } else {
            throw new RuntimeException("Unexpected principal type");
        }
    }

    private ParticipantDto convertToDto(Participant participant) {
        return new ParticipantDto(
                participant.getId(),
                participant.getJoinedAt(),
                participant.getEvent().getId(),
                participant.getUser() != null ? participant.getUser().getId() : null);
    }
}
