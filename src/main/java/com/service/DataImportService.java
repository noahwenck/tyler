package com.service;

import com.entity.Film;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repository.FilmRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class DataImportService {

    private final FilmRepository filmRepository;

    public DataImportService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public void importNetflix() {
        final RestTemplate restTemplate = new RestTemplate();

        final UriComponents shimizuUriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:5000")
                .path("netflix")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            final ResponseEntity<String> response =
                    restTemplate.getForEntity(shimizuUriComponents.toUriString(), String.class);
            List<Film> films = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
            filmRepository.saveAll(films);
        } catch (JsonProcessingException | RestClientException e) {
            // todo: handle exception types separately
            e.printStackTrace();
        }
    }
}
