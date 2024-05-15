package com.nessam.server.models;

import java.sql.Date;

public class CurrentJobPosition {
    private String userID;

    private String jobTitle;//40

    private int employmentType;//1-8
    private String companyName;//40
    private String workLocation;//40
    private int workplaceType;//1-3
    private boolean isActive;
    private Date startDate;
    private Date endDate;
    private String description;//1000
    private String skills;//40
    private boolean notifyChanges;
}
