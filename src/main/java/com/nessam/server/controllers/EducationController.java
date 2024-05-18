package com.nessam.server.controllers;

import com.nessam.server.dataAccess.EducationDAO;

import java.sql.SQLException;

public class EducationController {

    private final EducationDAO educationDAO;

    public EducationController() throws SQLException {
        this.educationDAO = new EducationDAO();

    }

//
//    public createEducation() {
//
//    }




}
