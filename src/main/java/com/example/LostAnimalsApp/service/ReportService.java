package com.example.LostAnimalsApp.service;

import com.example.LostAnimalsApp.dto.ReportDTO;

public interface ReportService {
    ReportDTO createReport(final ReportDTO reportDTO);
    ReportDTO updateReport(final ReportDTO reportDTO);
    ReportDTO deleteReport(final Long reportId);
}
