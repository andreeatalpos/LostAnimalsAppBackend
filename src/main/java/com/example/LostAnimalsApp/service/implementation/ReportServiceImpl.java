package com.example.LostAnimalsApp.service.implementation;

import com.example.LostAnimalsApp.dto.ReportDTO;
import com.example.LostAnimalsApp.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    @Override
    public ReportDTO createReport(ReportDTO reportDTO) {
        return null;
    }

    @Override
    public ReportDTO updateReport(ReportDTO reportDTO) {
        return null;
    }

    @Override
    public ReportDTO deleteReport(Long reportId) {
        return null;
    }
}
