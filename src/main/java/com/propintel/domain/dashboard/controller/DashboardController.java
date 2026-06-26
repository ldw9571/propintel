package com.propintel.domain.dashboard.controller;

import com.propintel.domain.dashboard.dto.DashboardSummaryDto;
import com.propintel.domain.dashboard.service.DashboardService;
import com.propintel.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryDto> summary() {
        return ApiResponse.ok(dashboardService.getSummary());
    }

    @GetMapping("/price-index")
    public ApiResponse<?> priceIndex(
            @RequestParam(defaultValue = "ALL") String area,
            @RequestParam(defaultValue = "24") int months) {
        return ApiResponse.ok(dashboardService.getPriceIndex(area, months));
    }

    @GetMapping("/volume")
    public ApiResponse<?> volume(
            @RequestParam(defaultValue = "12") int months) {
        return ApiResponse.ok(dashboardService.getMonthlyVolume(months));
    }

    @GetMapping("/top-regions")
    public ApiResponse<?> topRegions(
            @RequestParam(defaultValue = "5") int limit) {
        return ApiResponse.ok(dashboardService.getTopRegions(limit));
    }
}