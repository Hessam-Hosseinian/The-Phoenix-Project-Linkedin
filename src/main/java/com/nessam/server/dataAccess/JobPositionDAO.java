package com.nessam.server.dataAccess;

import com.nessam.server.models.JobPosition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobPositionDAO {

    private final Connection connection;

    public JobPositionDAO() throws SQLException {
        this.connection = DatabaseConnectionManager.getConnection();
        createJobPositionTable();
    }

    private void createJobPositionTable() throws SQLException {
        String jobPositionTableSql = """
                CREATE TABLE IF NOT EXISTS job_positions (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    userEmail VARCHAR(50) NOT NULL,
                    title VARCHAR(255) NOT NULL,
                    employment_type VARCHAR(50) NOT NULL,
                    company_name VARCHAR(255),
                    work_location VARCHAR(255),
                    work_location_type VARCHAR(50),
                    currently_working BOOLEAN,
                    start_date VARCHAR(50),
                    end_date VARCHAR(50),
                    description TEXT,
                    skills TEXT,
                    notify_network BOOLEAN,
                    FOREIGN KEY (userEmail) REFERENCES users(email) ON DELETE CASCADE
                )
                """;

        try (PreparedStatement statement = connection.prepareStatement(jobPositionTableSql)) {
            statement.executeUpdate();
        }
    }

    public void saveJobPosition(JobPosition jobPosition) throws SQLException {
        String jobPositionSql = """
                INSERT INTO job_positions (userEmail, title, employment_type, company_name, work_location, work_location_type, currently_working, start_date, end_date, description, skills, notify_network)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(jobPositionSql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            setJobPositionStatementParameters(statement, jobPosition);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Error saving job position: " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void updateJobPosition(JobPosition jobPosition) throws SQLException {
        String jobPositionSql = """
                UPDATE job_positions SET title = ?, employment_type = ?, company_name = ?, work_location = ?, work_location_type = ?, currently_working = ?, start_date = ?, end_date = ?, description = ?, skills = ?, notify_network = ? 
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(jobPositionSql)) {
            setJobPositionStatementParameters(statement, jobPosition);
            statement.setLong(12, jobPosition.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating job position: " + e.getMessage(), e);
        }
    }

    public void deleteJobPosition(long id) throws SQLException {
        String sql = "DELETE FROM job_positions WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error deleting job position: " + e.getMessage(), e);
        }
    }

    public JobPosition getJobPositionById(long id) throws SQLException {
        String sql = """
                SELECT id, userEmail, title, employment_type, company_name, work_location, work_location_type, currently_working, start_date, end_date, description, skills, notify_network
                FROM job_positions WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToJobPosition(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving job position by ID: " + e.getMessage(), e);
        }

        return null;
    }

    public List<JobPosition> getAllJobPositions() throws SQLException {
        String sql = """
                SELECT id, userEmail, title, employment_type, company_name, work_location, work_location_type, currently_working, start_date, end_date, description, skills, notify_network
                FROM job_positions
                """;

        List<JobPosition> jobPositions = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                jobPositions.add(mapResultSetToJobPosition(resultSet));
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving all job positions: " + e.getMessage(), e);
        }

        return jobPositions;
    }


    public List<JobPosition> getJobPositionsByUserEmail(String userEmail) throws SQLException {
        String sql = """
                SELECT id, userEmail, title, employment_type, company_name, work_location, work_location_type, currently_working, start_date, end_date, description, skills, notify_network
                FROM job_positions WHERE userEmail = ?
                """;

        List<JobPosition> jobPositions = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userEmail);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    jobPositions.add(mapResultSetToJobPosition(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving job positions by user email: " + e.getMessage(), e);
        }

        return jobPositions;
    }

    private void setJobPositionStatementParameters(PreparedStatement statement, JobPosition jobPosition) throws SQLException {
        statement.setString(1, jobPosition.getUserEmail());
        statement.setString(2, jobPosition.getTitle());
        statement.setString(3, jobPosition.getEmploymentType().name());
        statement.setString(4, jobPosition.getCompanyName());
        statement.setString(5, jobPosition.getWorkLocation());
        statement.setString(6, jobPosition.getWorkLocationType().name());
        statement.setBoolean(7, jobPosition.isCurrentlyWorking());
        statement.setString(8, jobPosition.getStartDate());
        statement.setString(9, jobPosition.getEndDate());
        statement.setString(10, jobPosition.getDescription());
        statement.setString(11, jobPosition.getSkills());
        statement.setBoolean(12, jobPosition.isNotifyNetwork());
    }

    private JobPosition mapResultSetToJobPosition(ResultSet resultSet) throws SQLException {
        JobPosition jobPosition = new JobPosition();

        jobPosition.setId(resultSet.getLong("id"));
        jobPosition.setUserEmail(resultSet.getString("userEmail"));
        jobPosition.setTitle(resultSet.getString("title"));
        jobPosition.setEmploymentType(JobPosition.EmploymentType.valueOf(resultSet.getString("employment_type")));
        jobPosition.setCompanyName(resultSet.getString("company_name"));
        jobPosition.setWorkLocation(resultSet.getString("work_location"));
        jobPosition.setWorkLocationType(JobPosition.WorkLocationType.valueOf(resultSet.getString("work_location_type")));
        jobPosition.setCurrentlyWorking(resultSet.getBoolean("currently_working"));
        jobPosition.setStartDate(resultSet.getString("start_date"));
        jobPosition.setEndDate(resultSet.getString("end_date"));
        jobPosition.setDescription(resultSet.getString("description"));
        jobPosition.setSkills(resultSet.getString("skills"));
        jobPosition.setNotifyNetwork(resultSet.getBoolean("notify_network"));

        return jobPosition;
    }
}
