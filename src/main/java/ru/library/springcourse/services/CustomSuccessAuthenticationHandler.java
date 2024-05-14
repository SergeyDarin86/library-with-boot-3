package ru.library.springcourse.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.library.springcourse.models.Person;
import ru.library.springcourse.securuty.PersonDetails;

import java.io.IOException;

@Component
public class CustomSuccessAuthenticationHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println("===============================================");
        System.out.println(personDetails.getPerson().getPersonId() + " <---- from Handler");
        System.out.println("===============================================");
        MDC.put("personId", String.valueOf(personDetails.getPerson().getPersonId()));

    }

}
