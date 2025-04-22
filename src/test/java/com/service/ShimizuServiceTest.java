package com.service;

import com.ShimizuProperties;
import com.entity.Film;
import com.entity.StreamingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repository.FilmRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ShimizuService}
 */
@ExtendWith(MockitoExtension.class)
class ShimizuServiceTest {

    @InjectMocks
    private ShimizuService shimizuService;
    @Mock
    private FilmRepository filmRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ShimizuProperties shimizuProperties;

    @Test
    void testImportFilmsFromService() {
        final ResponseEntity<String> response = mock(ResponseEntity.class);
        final List<Film> films = List.of(mock(Film.class));

        when(shimizuProperties.getShimizuUrl()).thenReturn("http://shimizu");
        when(restTemplate.getForEntity("http://shimizu/netflix", String.class)).thenReturn(response);
        try {
            when(objectMapper.readValue(eq(response.getBody()), any(TypeReference.class))).thenReturn(films);
        } catch (JsonProcessingException e) {
            fail(e);
        }
        when(filmRepository.saveAll(films)).thenReturn(null);

        final boolean success = shimizuService.importFilmsFromService(StreamingService.NETFLIX);

        assertTrue(success);
    }

    @Test
    void testImportFilmsFromServiceBadRequest() {
        when(shimizuProperties.getShimizuUrl()).thenReturn("http://shimizu");
        when(restTemplate.getForEntity("http://shimizu/netflix", String.class))
                .thenThrow(new RestClientException(""));

        final boolean success = shimizuService.importFilmsFromService(StreamingService.NETFLIX);

        assertFalse(success);
    }

    @Test
    void testImportFilmsFromServiceBadJson() throws JsonProcessingException {
        final ResponseEntity<String> response = mock(ResponseEntity.class);
        final List<Film> films = List.of(mock(Film.class));

        when(shimizuProperties.getShimizuUrl()).thenReturn("http://shimizu");
        when(restTemplate.getForEntity("http://shimizu/netflix", String.class)).thenReturn(response);
        when(objectMapper.readValue(eq(response.getBody()), any(TypeReference.class)))
                .thenThrow(new TestJsonProcessingException(""));

        final boolean success = shimizuService.importFilmsFromService(StreamingService.NETFLIX);

        assertFalse(success);
    }

    @Test
    void testImportFilmsFromServiceNullList() {
        final ResponseEntity<String> response = mock(ResponseEntity.class);
        final List<Film> films = List.of(mock(Film.class));

        when(shimizuProperties.getShimizuUrl()).thenReturn("http://shimizu");
        when(restTemplate.getForEntity("http://shimizu/netflix", String.class)).thenReturn(response);
        try {
            when(objectMapper.readValue(eq(response.getBody()), any(TypeReference.class))).thenReturn(films);
        } catch (JsonProcessingException e) {
            fail(e);
        }
        when(filmRepository.saveAll(films)).thenThrow(new IllegalArgumentException(""));

        final boolean success = shimizuService.importFilmsFromService(StreamingService.NETFLIX);

        assertFalse(success);
    }

    /**
     * For use in throwing {@link JsonProcessingException} in tests, since its constructors are all protected
     */
    public static final class TestJsonProcessingException extends JsonProcessingException {

        private TestJsonProcessingException(String msg) {
            super(msg);
        }
    }

}
