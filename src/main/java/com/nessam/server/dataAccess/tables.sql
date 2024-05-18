CREATE TABLE users
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    email               VARCHAR(50)  NOT NULL,
    password            VARCHAR(255) NOT NULL,
    first_name          VARCHAR(20),
    last_name           VARCHAR(40),
    additional_name     VARCHAR(40),
    profile_picture     VARCHAR(255),
    background_picture  VARCHAR(255),
    title               VARCHAR(220),
    location            VARCHAR(255),
    profession          VARCHAR(255),
    seeking_opportunity VARCHAR(255)
);

CREATE TABLE education
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                BIGINT      NOT NULL,
    school_name            VARCHAR(40) ,
    field_of_study         VARCHAR(40) ,
    education_start_date   DATE,
    education_end_date     DATE,
    grade                  VARCHAR(40),
    activities_description VARCHAR(500),
    description            VARCHAR(1000),
    skills                 VARCHAR(40),
    notify_changes         BOOLEAN     ,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);


CREATE TABLE current_job_positions
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    job_title       VARCHAR(40),
    employment_type INT,
    company_name    VARCHAR(40),
    work_location   VARCHAR(40),
    workplace_type  INT,
    is_active       BOOLEAN,
    start_date      DATE,
    end_date        DATE,
    description     VARCHAR(1000),
    skills          VARCHAR(40),
    notify_changes  BOOLEAN,
    CONSTRAINT fk_user_cjp
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);


CREATE TABLE contact_information
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                BIGINT NOT NULL,
    profile_link           VARCHAR(40),
    email                  VARCHAR(40),
    phone_number           VARCHAR(40),
    phone_type             INT,
    address                VARCHAR(220),
    birth_month            DATE,
    birth_day              DATE,
    birth_privacy_policy   INT,
    instant_contact_method VARCHAR(40),
    CONSTRAINT fk_user_ci
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);


