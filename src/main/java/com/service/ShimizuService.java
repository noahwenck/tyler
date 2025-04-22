package com.service;

import com.ShimizuProperties;
import com.entity.Film;
import com.entity.StreamingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repository.FilmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Service for accessing Shimizu API
 */
@Service
public class ShimizuService {

    private static final Logger LOG = LoggerFactory.getLogger(ShimizuService.class);

    private final FilmRepository filmRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ShimizuProperties shimizuProperties;

    public ShimizuService(FilmRepository filmRepository,
                          ObjectMapper objectMapper,
                          RestTemplate restTemplate,
                          ShimizuProperties shimizuProperties) {
        this.filmRepository = filmRepository;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.shimizuProperties = shimizuProperties;
    }

    /**
     * Import expiring films from Shimizu API
     *
     * @param streamingService which streaming service we are importing from
     */
    public boolean importFilmsFromService(StreamingService streamingService) {
        final UriComponents shimizuUriComponents = UriComponentsBuilder
                .fromHttpUrl(shimizuProperties.getShimizuUrl())
                .path(String.valueOf(streamingService).toLowerCase())
                .build();

        LOG.info("Attempting to import films from URI={}", shimizuUriComponents.toUriString());

        try {
            final ResponseEntity<String> response =
                    restTemplate.getForEntity(shimizuUriComponents.toUriString(), String.class);

            // Convert Json to Film objects
            List<Film> films = objectMapper.readValue(response.getBody(), new TypeReference<>() {});

            LOG.info("Saving imported films to db, numberOfFilms={}", films.size());
            filmRepository.saveAll(films);
            return true;
        } catch (RestClientException e) {
            LOG.error("Failure to during HTTP request, URI={}", shimizuUriComponents.toUriString(), e);
        } catch (JsonProcessingException e) {
            LOG.error("Failure to parse Shimizu response", e);
        } catch (IllegalArgumentException e) {
            LOG.error("Imported films list is null, or a list item was null, URI={}",
                    shimizuUriComponents.toUriString(),
                    e);
        }

        return false;
    }
}
