package com.controller;

import com.Controller.ShimizuController;
import com.entity.StreamingService;
import com.service.ShimizuService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link com.Controller.ShimizuController}
 */
@ExtendWith(MockitoExtension.class)
class ShimizuControllerTest {

    @InjectMocks
    private ShimizuController shimizuController;
    @Mock
    private ShimizuService shimizuService;

    @Test
    void testImportFilmFromService() {
        when(shimizuService.importFilmsFromService(StreamingService.NETFLIX)).thenReturn(true);

        final ResponseEntity<Void> response =
                shimizuController.importFilmFromService("netflix");

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testImportFilmFromServiceInvalidService() {
        final ResponseEntity<Void> response =
                shimizuController.importFilmFromService("not a service");

        assertTrue(response.getStatusCode().is5xxServerError());
    }
}
