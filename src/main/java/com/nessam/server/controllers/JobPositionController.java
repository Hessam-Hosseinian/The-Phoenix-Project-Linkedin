package com.nessam.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.JobPositionDAO;
import com.nessam.server.models.JobPosition;
//import com.nessam.server.models.JobRequirements;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

public class JobPositionController {
    private final JobPositionDAO jobPositionDAO;
    private final ObjectMapper objectMapper;

    public JobPositionController() throws SQLException {
        this.jobPositionDAO = new JobPositionDAO();
        this.objectMapper = new ObjectMapper();
    }


    public String getJobPositions() throws SQLException, JsonProcessingException {

        List<JobPosition> jobPositions = jobPositionDAO.getAllJobPositions();

        return objectMapper.writeValueAsString(jobPositions);
    }

    public String getJobPositionsByUserEmail(String email) throws SQLException, JsonProcessingException {


        List<JobPosition> jobPositions = jobPositionDAO.getJobPositionsByUserEmail(email);

        return objectMapper.writeValueAsString(jobPositions);
    }



    public void createJobPosition(    JobPosition jobPosition ) throws SQLException {
//        JobPosition jobPosition = jsonToJobPosition(jsonObject);


        jobPositionDAO.saveJobPosition(jobPosition);

    }


    private JobPosition jsonToJobPosition(JSONObject jsonObject) {
        JobPosition jobPosition = new JobPosition();

        jobPosition.setId(jsonObject.getLong("Id"));
        jobPosition.setUserEmail(jsonObject.getString("userEmail"));
        jobPosition.setTitle(jsonObject.getString("title"));
        jobPosition.setEmploymentType(JobPosition.EmploymentType.valueOf(jsonObject.getString("employmentType")));
        jobPosition.setCompanyName(jsonObject.getString("company_name"));
        jobPosition.setWorkLocation(jsonObject.getString("work_location"));
        jobPosition.setWorkLocationType(JobPosition.WorkLocationType.valueOf(jsonObject.getString("work_location_type")));
        jobPosition.setCurrentlyWorking(jsonObject.getBoolean("currently_working"));
        jobPosition.setStartDate(jsonObject.getString("start_date"));
        jobPosition.setEndDate(jsonObject.getString("end_date"));
        jobPosition.setDescription(jsonObject.getString("description"));
        jobPosition.setSkills(jsonObject.getString("skills"));


        return jobPosition;
    }


}
