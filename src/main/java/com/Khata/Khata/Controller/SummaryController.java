package com.Khata.Khata.Controller;

import com.Khata.Khata.Dto.MonthSummary;
import com.Khata.Khata.Service.SummaryService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/summary")
public class SummaryController
{
    private final SummaryService summaryService;

    SummaryController(SummaryService summaryService)
    {
        this.summaryService = summaryService;
    }

    @GetMapping
    public MonthSummary getSummary(Authentication auth, @RequestParam int year, @RequestParam int month)
    {
        return summaryService.getSummary(userId(auth), year, month);
    }

    @PostMapping("/lock")
    public MonthSummary lock(Authentication auth, @RequestParam int year, @RequestParam int month)
    {
        return summaryService.lock(userId(auth), year, month);
    }

    @PostMapping("/unlock")
    public MonthSummary unlock(Authentication auth, @RequestParam int year, @RequestParam int month)
    {
        return summaryService.unlock(userId(auth), year, month);
    }

    @GetMapping("/export.csv")
    public ResponseEntity<byte[]> exportCsv(Authentication auth, @RequestParam int year, @RequestParam int month)
    {
        MonthSummary summary = summaryService.getSummary(userId(auth), year, month);
        byte[] csv = summaryService.buildCsv(summary).getBytes(StandardCharsets.UTF_8);
        String filename = String.format("classtracker-%d-%02d.csv", year, month);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }

    private Integer userId(Authentication auth)
    {
        return (Integer) auth.getPrincipal();
    }
}
