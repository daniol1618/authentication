package com.gl.authentication.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

public class CreateUserRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=(?:[^A-Z]*[A-Z][^A-Z]*$))(?=(?:\\D*\\d\\D*\\d\\D*$))[a-zA-Z\\d]{8,12}$",
            message = "La contraseña debe tener una mayúscula y exactamente dos números"
    )

    private String password;

    private String name; // opcional

    private List<PhoneRequest> phones; // opcional

    // getters y setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PhoneRequest> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneRequest> phones) {
        this.phones = phones;
    }
}
