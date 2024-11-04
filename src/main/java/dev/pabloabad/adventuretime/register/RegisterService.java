package dev.pabloabad.adventuretime.register;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import dev.pabloabad.adventuretime.dtos.RegisterDto;
import dev.pabloabad.adventuretime.facades.EncoderFacade;
import dev.pabloabad.adventuretime.facades.implementations.IEncryptFacade;
import dev.pabloabad.adventuretime.profiles.Profile;
import dev.pabloabad.adventuretime.profiles.ProfileService;
import dev.pabloabad.adventuretime.roles.Role;
import dev.pabloabad.adventuretime.roles.RoleService;
import dev.pabloabad.adventuretime.roles.exceptions.RoleNotFoundException;
import dev.pabloabad.adventuretime.users.User;
import dev.pabloabad.adventuretime.users.UserRepository;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ProfileService profileService;
    private final IEncryptFacade encoderFacade;

    public RegisterService(UserRepository userRepository, RoleService roleService, ProfileService profileService,
            EncoderFacade encoderFacade) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.profileService = profileService;
        this.encoderFacade = encoderFacade;
    }

    public User save(RegisterDto newRegisterDto) {
        String passwordEncoded = encoderFacade.encode("bcrypt", newRegisterDto.getPassword());

        User user = new User(newRegisterDto.getUsername(), passwordEncoded);
        user.setRoles(assignDefaultRole());

        User savedUser = userRepository.save(user);

        Profile profile = new Profile(newRegisterDto.getEmail(), savedUser);
        profileService.save(profile);

        return savedUser;
    }

    public Set<Role> assignDefaultRole() throws RoleNotFoundException {
        Set<Role> roles = new HashSet<>();
        Role defaultRole = roleService.getById(1L);
        roles.add(defaultRole);
        return roles;
    }
}
