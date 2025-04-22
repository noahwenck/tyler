package com.Controller;

import com.entity.StreamingService;
import com.service.ShimizuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for accessing Shimizu API
 */
@Controller
@RequestMapping("shimizu")
public class ShimizuController {

    private static final Logger LOG = LoggerFactory.getLogger(ShimizuController.class);

    private final ShimizuService shimizuService;

    public ShimizuController(ShimizuService shimizuService) {
        this.shimizuService = shimizuService;
    }

    /**
     * Endpoint to populate database for films of a certain streaming service
     *
     * @param streamingService which streaming service to import films from
     */
    @GetMapping("/{streamingService}")
    public ResponseEntity<Void> importFilmFromService(@PathVariable String streamingService) {
        try {
            final StreamingService serviceEnum = StreamingService.valueOf(streamingService.toUpperCase());
            final boolean importSuccess = shimizuService.importFilmsFromService(serviceEnum);

            if (importSuccess) {
                LOG.info("Successfully imported and saved films from streamingService={}", streamingService);
                return ResponseEntity.ok().build();
            }
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid streamingService={}", streamingService);
        }
        return ResponseEntity.internalServerError().build();
    }
}
