package com.example.appjwtrealemailauditing.component;

import com.example.appjwtrealemailauditing.entity.Role;
import com.example.appjwtrealemailauditing.entity.enums.RoleName;
import com.example.appjwtrealemailauditing.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Value("${spring.datasource.initialization-mode}")
    private String initValue;

    @Override
    public void run(String... args) throws Exception {
        if (initValue.equals("always"))
        {
            Role adminRole = roleRepository.save(new Role(RoleName.ROLE_ADMIN));
            Role moderRole = roleRepository.save(new Role(RoleName.ROLE_MODER));
            Role userRole = roleRepository.save(new Role(RoleName.ROLE_USER));
        }
    }
}
