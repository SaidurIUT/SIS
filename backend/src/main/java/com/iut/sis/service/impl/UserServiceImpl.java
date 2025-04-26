package com.iut.sis.service.impl;


import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


import com.iut.sis.entity.Role;
import com.iut.sis.entity.User;
import com.iut.sis.exception.ApiException;
import com.iut.sis.exception.ResourceNotFoundException;
import com.iut.sis.payloads.AdminInfoDto;
import com.iut.sis.payloads.StudentInfoDto;
import com.iut.sis.payloads.TeacherInfoDto;
import com.iut.sis.payloads.UserDto;
import com.iut.sis.repository.RoleRepo;
import com.iut.sis.repository.UserRepo;
import com.iut.sis.service.AdminService;
import com.iut.sis.service.StudentService;
import com.iut.sis.service.TeacherService;
import com.iut.sis.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.dtoToUser(userDto);
        User savedUser = this.userRepo.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());
        user.setAge(userDto.getAge());
        user.setImageName(userDto.getImageName());

        User updatedUser = this.userRepo.save(user);
        return this.userToDto(updatedUser);
    }


    @Override
    public UserDto getUserById(Integer userId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

        return this.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {

        List<User> users = this.userRepo.findAll();
        List<UserDto> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());

        return userDtos;
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        this.userRepo.delete(user);

    }

    public User dtoToUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        return user;
    }

    public UserDto userToDto(User user) {
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        return userDto;
    }

    @Transactional
    @Override
    public UserDto registerNewUser(UserDto userDto, Integer roleId) {
        User user = this.modelMapper.map(userDto, User.class);

        // Encode the password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));


        // Get the role
        Role role = this.roleRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "Id", roleId));

        user.getRoles().add(role);



        if(roleId==502) {
            user.setImageName("student.png");
        }
        if (roleId == 503) {
            user.setImageName("teacher.png");
        }
        if (roleId == 501) {
            user.setImageName("admin.png");
        }

        User newUser = this.userRepo.save(user);

        if(roleId == 501){
            AdminInfoDto adminInfoDto = new AdminInfoDto();
            adminService.registerAdmin(adminInfoDto, newUser.getId());
        }
        else if(roleId == 502){
            StudentInfoDto studentInfoDto = new StudentInfoDto();
            studentService.registerStudent(studentInfoDto, newUser.getId());
        }
        else if(roleId == 503){
            TeacherInfoDto teacherInfoDto = new TeacherInfoDto();
            teacherService.registerTeacher(teacherInfoDto, newUser.getId());
        }
        return this.modelMapper.map(newUser, UserDto.class);
    }



    @Override
    public String validateVerificationToken(String token) {
        User user = userRepo.findByVerificationToken(token).orElse(null);
        if (user == null) {
            return "invalid";
        }

        user.setEnabled(true);
        userRepo.save(user);
        return "valid";
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email"));
        if (user == null) {
            return "You are not registered yet";
        }
        else{
            String token = UUID.randomUUID().toString();
            user.setVerificationToken(token);
            userRepo.save(user);

            try {
                forgotPasswordEmail(email, token);
            } catch (MessagingException e) {
                return "Failed to send reset password email";
            }

            return "A email is sent to your email address";
        }
    }

    @Override
    public String resetPassword(String token, String newPassword) {
        User user = userRepo.findByVerificationToken(token).orElseThrow(() -> new ApiException("Invalid or expired reset token."));

        if (user == null) {
            return "You are not valid user to reset password";
        }
        else{
            user.setPassword(this.passwordEncoder.encode(newPassword));
            String newToken = UUID.randomUUID().toString();
            user.setVerificationToken(newToken);
            userRepo.save(user);
            return "Your new password is set properly!!!";
        }

    }

    // Send forgot password email to the user
    private void forgotPasswordEmail(String email, String token) throws MessagingException {
        User user = userRepo.findByEmail(email).orElse(null);

        // Generate a password reset URL using the token
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(email);
        helper.setSubject("Reset Your Password - Health Pulse");

        // HTML content for password reset email
        String emailContent = "<html>"
                + "<body style='font-family: Arial, sans-serif; background-color: #f4f9ff; padding: 20px;'>"
                + "<div style='max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>"
                + "<h2 style='color: #1E90FF; text-align: center;'>Forgot Your Password?</h2>"
                + "<p style='font-size: 16px; color: #333333; line-height: 1.6;'>"
                + "No worries, we’ve got you covered! You can reset your password by clicking the button below."
                + "</p>"
                + "<div style='text-align: center; margin: 20px 0;'>"
                + "<a href='" + resetUrl + "' style='background-color: #1E90FF; color: white; padding: 10px 20px; border-radius: 5px; text-decoration: none;'>"
                + "Reset Password</a>"
                + "</div>"
                + "<p style='font-size: 16px; color: #333333;'>"
                + "If you did not request this password reset, please ignore this email. This link will expire in 24 hours."
                + "</p>"
                + "<p style='font-size: 16px; color: #333333;'>"
                + "Best regards,<br><strong>Health Pulse Team</strong>"
                + "</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }






}