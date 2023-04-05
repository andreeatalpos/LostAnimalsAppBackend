package com.example.LostAnimalsApp.service.implementation;

import com.example.LostAnimalsApp.dto.ReportDTO;
import com.example.LostAnimalsApp.exception.ResourceNotFoundException;
import com.example.LostAnimalsApp.model.Animal;
import com.example.LostAnimalsApp.model.Report;
import com.example.LostAnimalsApp.model.User;
import com.example.LostAnimalsApp.repository.AnimalRepository;
import com.example.LostAnimalsApp.repository.ReportRepository;
import com.example.LostAnimalsApp.repository.UserRepository;
import com.example.LostAnimalsApp.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;
    @Override
    public ReportDTO createReport(final ReportDTO reportDTO) {
        if (checkFields(reportDTO)) {
            var report = Report.builder()
                    .title(reportDTO.getTitle())
                    .description(reportDTO.getDescription())
                    .animal(modelMapper.map(reportDTO.getAnimal(), Animal.class))
                    .user(modelMapper.map(reportDTO.getUser(), User.class))
                    .build();
            reportRepository.save(report);
            return modelMapper.map(report, ReportDTO.class);
        } else throw new ResourceNotFoundException("The report cannot be created!");
    }

    @Override
    public ReportDTO updateReport(final ReportDTO reportDTO) {
        Report reportToUpdate = reportRepository.findById(reportDTO.getReportId()).orElse(null);
        if (reportToUpdate != null && checkFields(reportDTO)) {
            reportToUpdate = Report.builder()
                    .title(reportDTO.getTitle())
                    .description(reportDTO.getDescription())
                    .animal(modelMapper.map(reportDTO.getAnimal(), Animal.class))
                    .user(modelMapper.map(reportDTO.getUser(), User.class))
                    .build();
            reportRepository.save(reportToUpdate);
            return modelMapper.map(reportToUpdate, ReportDTO.class);
        } else throw new ResourceNotFoundException("The report cannot be updated!");
    }

    @Override
    public ReportDTO deleteReport(final Long reportId) {
        Report reportToDelete = reportRepository.findById(reportId).orElse(null);
        if (reportToDelete != null) {
            reportRepository.delete(reportToDelete);
            return modelMapper.map(reportToDelete, ReportDTO.class);
        } else throw new ResourceNotFoundException("The report with this ID doesn't exists!");
    }

    private boolean checkFields(final ReportDTO reportDTO) {
        if (reportDTO.getTitle() == null || reportDTO.getTitle().isBlank()) {
            return false;
        }
        if (reportDTO.getDescription() == null || reportDTO.getDescription().isBlank()) {
            return false;
        }
        if (reportDTO.getUser() == null || userRepository.findByUsername(reportDTO.getUser().getUsername()).isEmpty()) {
            return false;
        }
        if (reportDTO.getAnimal() == null || animalRepository.findById(reportDTO.getAnimal().getAnimalId()).isEmpty()) {
            return false;
        }
        return true;
    }
}
