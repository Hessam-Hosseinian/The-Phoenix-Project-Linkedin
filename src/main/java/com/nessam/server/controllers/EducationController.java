//package com.nessam.server.controllers;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nessam.server.dataAccess.EducationDAO;
//import com.nessam.server.models.Education;
//
//import java.sql.SQLException;
//import java.util.List;
//
//public class EducationController {
//    private final EducationDAO educationDAO;
//    private final ObjectMapper objectMapper;
//
//    public EducationController() throws SQLException {
//        this.educationDAO = new EducationDAO();
//        this.objectMapper = new ObjectMapper();
//    }
//
//    public void createEducation( String userEmail, String institution, String degree, String fieldOfStudy, String startDate, String endDate) throws SQLException {
//       Education education  =new Education();
//       education.setUserEmail(userEmail);
//       education.setInstitution(institution);
//       education.setDegree(degree);
//       education.setFieldOfStudy(fieldOfStudy);
//       education.setStartDate(startDate);
//       education.setEndDate(endDate);
//
//
//        if (isEducationExists(userEmail)) {
//            educationDAO.updateEducation(education);
//        } else {
//            education.setUserEmail(userEmail);
//            educationDAO.saveEducation(education);
//        }
//    }
//
//    public boolean isEducationExists(String userEmail) {
//        return educationDAO.getEducationsByUserEmail(userEmail) != null;
//    }
//
//    public String getAllEducations() throws SQLException, JsonProcessingException {
//        List<Education> educations = educationDAO.getAllEducations();
//        return objectMapper.writeValueAsString(educations);
//    }
//
//    public String getEducationsByUser(String userEmail) throws SQLException, JsonProcessingException {
//        List<Education> educations = educationDAO.getEducationsByUserEmail(userEmail);
//        return objectMapper.writeValueAsString(educations);
//    }
//
//    public String getEducationById(Long educationId) throws SQLException, JsonProcessingException {
//        Education education = educationDAO.getEducationById(educationId);
//        return education != null ? objectMapper.writeValueAsString(education) : "No Education";
//    }
//
//    public void updateEducation(Long educationId, Education updatedEducation) throws SQLException {
//        Education existingEducation = educationDAO.getEducationById(educationId);
//        if (existingEducation != null) {
//            updatedEducation.setId(educationId);
//            educationDAO.updateEducation(updatedEducation);
//        }
//    }
//
//    public void deleteEducation(Long educationId) {
//        educationDAO.deleteEducation(educationId);
//    }
//}
////this is a test comment
//
