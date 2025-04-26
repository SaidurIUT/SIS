package com.iut.sis.service.impl;

import com.iut.sis.entity.AdminInfo;
import com.iut.sis.entity.Role;
import com.iut.sis.entity.User;
import com.iut.sis.exception.ResourceNotFoundException;
import com.iut.sis.payloads.AdminInfoDto;
import com.iut.sis.repository.AdminInfoRepository;
import com.iut.sis.repository.RoleRepo;
import com.iut.sis.repository.UserRepo;
import com.iut.sis.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminInfoRepository adminRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AdminInfoDto registerAdmin(AdminInfoDto adminInfoDto, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        // Ensure user has admin role
        Role adminRole = this.roleRepo.findById(501) // Assuming 501 is admin role id
                .orElseThrow(() -> new ResourceNotFoundException("Role", "Id", 501));

        if (!user.getRoles().contains(adminRole)) {
            user.getRoles().add(adminRole);
            userRepo.save(user);
        }

        AdminInfo adminInfo = this.modelMapper.map(adminInfoDto, AdminInfo.class);
        adminInfo.setUser(user);

        AdminInfo savedAdmin = this.adminRepository.save(adminInfo);
        return this.modelMapper.map(savedAdmin, AdminInfoDto.class);
    }

    @Override
    public AdminInfoDto updateAdminInfo(AdminInfoDto adminInfoDto, Integer adminId) {
        AdminInfo adminInfo = this.adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin", "Id", adminId));

        if (adminInfoDto.getAdminRole() != null) {
            adminInfo.setAdminRole(adminInfoDto.getAdminRole());
        }

        if (adminInfoDto.getResponsibilities() != null) {
            adminInfo.setResponsibilities(adminInfoDto.getResponsibilities());
        }

        AdminInfo updatedAdmin = this.adminRepository.save(adminInfo);
        return this.modelMapper.map(updatedAdmin, AdminInfoDto.class);
    }

    @Override
    public AdminInfoDto getAdminById(Integer adminId) {
        AdminInfo adminInfo = this.adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin", "Id", adminId));
        return this.modelMapper.map(adminInfo, AdminInfoDto.class);
    }

    @Override
    public AdminInfoDto getAdminByUserId(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        if (user.getAdminInfo() == null) {
            throw new ResourceNotFoundException("Admin Info", "User Id", userId);
        }

        return this.modelMapper.map(user.getAdminInfo(), AdminInfoDto.class);
    }

    @Override
    public List<AdminInfoDto> getAllAdmins() {
        List<AdminInfo> admins = this.adminRepository.findAll();
        return admins.stream()
                .map(admin -> this.modelMapper.map(admin, AdminInfoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAdmin(Integer adminId) {
        AdminInfo adminInfo = this.adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin", "Id", adminId));
        this.adminRepository.delete(adminInfo);
    }
}