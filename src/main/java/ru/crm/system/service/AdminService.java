package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.repository.AdminRepository;
import ru.crm.system.dto.admin.AdminCreateEditDto;
import ru.crm.system.dto.admin.AdminReadDto;
import ru.crm.system.mapper.AdminCreateEditMapper;
import ru.crm.system.mapper.AdminReadMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminCreateEditMapper adminCreateEditMapper;
    private final AdminReadMapper adminReadMapper;

    @Transactional
    public AdminReadDto create(AdminCreateEditDto createDto) {
        return Optional.of(createDto)
                .map(adminCreateEditMapper::map)
                .map(adminRepository::save)
                .map(adminReadMapper::map)
                .orElseThrow();
    }

    public Optional<AdminReadDto> findById(Integer id) {
        return adminRepository.findById(id)
                .map(adminReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return adminRepository.findById(id)
                .map(admin -> {
                    adminRepository.delete(admin);
                    adminRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}