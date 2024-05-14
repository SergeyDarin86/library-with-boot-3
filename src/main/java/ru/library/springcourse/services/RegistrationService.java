package ru.library.springcourse.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.springcourse.models.Person;
import ru.library.springcourse.repositories.PeopleRepository;

@Slf4j
@Service
@AllArgsConstructor
public class RegistrationService {

    private final PeopleRepository peopleRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(Person person) {
        log.info("Start method register(person) for RegistrationService, person is: {}", person);
        person.setRole("ROLE_USER");
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        peopleRepository.save(person);
    }

}
