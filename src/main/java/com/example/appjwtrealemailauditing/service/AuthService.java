package com.example.appjwtrealemailauditing.service;

import com.example.appjwtrealemailauditing.entity.User;
import com.example.appjwtrealemailauditing.entity.enums.RoleName;
import com.example.appjwtrealemailauditing.payload.ApiResponse;
import com.example.appjwtrealemailauditing.payload.LoginDto;
import com.example.appjwtrealemailauditing.payload.RegisterDto;
import com.example.appjwtrealemailauditing.repository.RoleRepository;
import com.example.appjwtrealemailauditing.repository.UserRepository;
import com.example.appjwtrealemailauditing.security.JwtProvider;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.aspectj.weaver.patterns.IToken;
import org.omg.CORBA.BAD_CONTEXT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse registerUser(RegisterDto registerDto){
        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail) {
                return new ApiResponse("Bunday email allaqachon mavjud",false);
        }
                User user = new User();
                user.setFirstName(registerDto.getFirstName());
                user.setLastName(registerDto.getLastName());
                user.setEmail(registerDto.getEmail());
                user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
                user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)));
                user.setEmailCode(UUID.randomUUID().toString());
                userRepository.save(user);
                //EMAILGA YUBORISH METHODINI CHAQIRYAPMIZ
                sendEmail(user.getEmail(),user.getEmailCode());
                return new ApiResponse("Muvaffaqqiyatli ro'yxatdan o'tdingiz. Akkounting aktivlashtirilishi uchun emailingizni tasdiqlang",true);
        }
        public Boolean sendEmail(String sendingEmail,String emailCode){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("sirojiddin1712@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Akkountni tasdiqlash");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode="+emailCode+"&email="+sendingEmail+"'>tasdiqlang</a>");
            javaMailSender.send(mailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> byEmailAndEmailCode = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (byEmailAndEmailCode.isPresent()){
            User user = byEmailAndEmailCode.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Akkount tasdiqlandi",true);
        }
        return new ApiResponse("Akkount allaqachon tasdiqlangan",false);
    }
    public  ApiResponse login(LoginDto loginDto){
        try{
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()));
        User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return new ApiResponse("Token",true,token);
        }catch (BadCredentialsException badCredentialsException){
            return new ApiResponse("parol yoki login hato",false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User>optionalUser = userRepository.findByEmail(username);
//
//        if (optionalUser.isPresent())
//            return optionalUser.get();
//        throw new UsernameNotFoundException(username+" topilmadi");

        return userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username+" topilmadi"));


    }
}
