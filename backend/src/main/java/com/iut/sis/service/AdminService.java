package com.iut.sis.service;

import com.iut.sis.payloads.AdminInfoDto;
import java.util.List;

public interface AdminService {
    AdminInfoDto registerAdmin(AdminInfoDto adminInfoDto, Integer userId);
    AdminInfoDto updateAdminInfo(AdminInfoDto adminInfoDto, Integer adminId);
    AdminInfoDto getAdminById(Integer adminId);
    AdminInfoDto getAdminByUserId(Integer userId);
    List<AdminInfoDto> getAllAdmins();
    void deleteAdmin(Integer adminId);
}