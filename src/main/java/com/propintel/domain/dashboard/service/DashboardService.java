package com.propintel.domain.dashboard.service;

import com.propintel.domain.dashboard.dto.DashboardSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    public DashboardSummaryDto getSummary() {
        // 실제 DB 연동 전 임시 데이터
        return new DashboardSummaryDto(50000L, 5000L, 65.0, 0.5);
    }

    public List<?> getPriceIndex(String area, int months) {
        return new ArrayList<>();
    }

    public List<?> getMonthlyVolume(int months) {
        return new ArrayList<>();
    }

    public List<?> getTopRegions(int limit) {
        return new ArrayList<>();
    }
}