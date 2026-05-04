package com.foodtrack.spring.springboot_application.entrypoint.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

    private static final Logger logger = LoggerFactory.getLogger(AdminReportController.class);

    private final RestClient restClient;
    private final String reportServiceBaseUrl;

    public AdminReportController(
            RestClient.Builder restClientBuilder,
            @Value("${report-service.base-url:http://localhost:8081}") String reportServiceBaseUrl
    ) {
        this.reportServiceBaseUrl = Objects.requireNonNull(reportServiceBaseUrl, "reportServiceBaseUrl");
        this.restClient = restClientBuilder.baseUrl(this.reportServiceBaseUrl).build();
    }

    @PostMapping("/generate-now")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> generateNow(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        logger.info("Manual report generation requested through Backend_FoodTrack. date={}, reportService={}", date, reportServiceBaseUrl);

        try {
            Map<String, Object> reportServiceResponse = restClient.post()
                    .uri(uriBuilder -> {
                        var builder = uriBuilder.path("/api/reports/generate-and-send");
                        if (date != null) {
                            builder.queryParam("date", date);
                        }
                        return builder.build();
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", true);
            response.put("message", "ReportService ejecuto la generacion manual.");
            response.put("reportService", reportServiceResponse);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("ReportService manual generation failed: {}", e.getMessage(), e);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", false);
            response.put("message", "No se pudo generar el reporte desde ReportService.");
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
