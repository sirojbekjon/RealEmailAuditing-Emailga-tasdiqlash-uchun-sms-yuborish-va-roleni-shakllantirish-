package com.example.appjwtrealemailauditing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User implements UserDetails{

    @Id
    @GeneratedValue
    private UUID id; //userning takrorlanmas id si

    @Column(nullable = false,length = 50) // length buyerda databazaga maximal qiymati 50 ekanligini bildiradi
    private String firstName;//userning ismi

    @Column(nullable = false)
    private String lastName;//userning familyasi


    @Email
    @Column(nullable = false,unique = true)
    private String email;//example@gmail.com userning emaili(USERNAME SIFATIDA OLINADI)

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;//ro'yxatdan o'tgan sanasi


    @UpdateTimestamp //yoki bolmasa '@LastModifiedDate' dan foydalans ham boladi
    private Timestamp updatedAt;//oxirgi marta qachon o'zgartirganligi


    @ManyToMany
    private Set<Role> roles;






    private boolean accountNonExpired = true; //Bu userning amal qilish muddati o'tmagan

    private boolean accountNonLocked = true; //Bu user (accauntning) bloklanmaganligini bildiradi

    private boolean credentialsNonExpired = true;

    private boolean enabled;

    private String emailCode;

    //-------------BU USERDETAILS NING METHODLARI-----------------////////


    //BU USERNING HUQUQLARI RO'YXATI
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    //USERNING USERNAMENI QAYTARUVCHI METHOD
    @Override
    public String getUsername() {
        return this.email;
    }
    //ACCAUNTNING AMAL QILISH MUDDATINI QAYTARADI
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    //ACCAUNT BLOKLANGANLIGI HOLATINI QAYTARADI
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }
    //ACCAUNTNING ISHONCHLILIK MUDDATI TUGAGAN YOKI TUGAMAGANLIGINI QAYTARADI
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }
    //ACCAUNTNING YONIQ YOKI O'CHIQLIGINI QAYTARADI
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
