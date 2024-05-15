package com.nessam.server.models;

import java.sql.Date;

public class ContactInformation {
    private String profileLink;//40
    private String email;//40
    private String phoneNumber;//40
    private int phoneType;//1-3
    private String address;//220
    private Date birthMonth;
    private Date birthDay;
    private int birthPrivacyPolicy;//1-5
    private String instantContactMethod; //40

}
