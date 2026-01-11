package com.gl.authentication.entity;

import javax.persistence.*;

@Entity
@Table(name = "phones")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;
    private String cityCode;
    private String countryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // getters y setters
    public Long getId() { return id; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getCityCode() { return cityCode; }
    public void setCityCode(String cityCode) { this.cityCode = cityCode; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
