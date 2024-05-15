CREATE TABLE Users
(
    ID                 INT PRIMARY KEY AUTO_INCREMENT,
    email              VARCHAR(255),
    password           VARCHAR(255),
    firstName          VARCHAR(20),
    lastName           VARCHAR(40),
    additionalName     VARCHAR(40),
    profilePicture     VARCHAR(255),
    backgroundPicture  VARCHAR(255),
    title              VARCHAR(220),
    location           VARCHAR(255),
    profession         VARCHAR(255),
    seekingOpportunity VARCHAR(255)
);

CREATE TABLE CurrentJobPosition
(
    userID         INT PRIMARY KEY AUTO_INCREMENT,
    jobTitle       VARCHAR(40),
    employmentType INT,
    companyName    VARCHAR(40),
    workLocation   VARCHAR(40),
    workplaceType  INT,
    isActive       BOOLEAN,
    startDate      DATE,
    endDate        DATE,
    description    VARCHAR(1000),
    skills         VARCHAR(40),
    notifyChanges  BOOLEAN,
    FOREIGN KEY (userID) REFERENCES Users (ID)
);

CREATE TABLE Education
(
    userID                INT PRIMARY KEY AUTO_INCREMENT,
    schoolName            VARCHAR(40),
    fieldOfStudy          VARCHAR(40),
    educationStartDate    DATE,
    educationEndDate      DATE,
    grade                 VARCHAR(40),
    activitiesDescription VARCHAR(500),
    description           VARCHAR(1000),
    skills                VARCHAR(40),
    notifyChanges         BOOLEAN,
    FOREIGN KEY (userID) REFERENCES Users (ID)
);

CREATE TABLE ContactInformation
(
    userID               INT PRIMARY KEY AUTO_INCREMENT,
    profileLink          VARCHAR(40),
    email                VARCHAR(40),
    phoneNumber          VARCHAR(40),
    phoneType            INT,
    address              VARCHAR(220),
    birthMonth           DATE,
    birthDay             DATE,
    birthPrivacyPolicy   INT,
    instantContactMethod VARCHAR(40),
    FOREIGN KEY (userID) REFERENCES Users (ID)
);

INSERT INTO Users (email, password, firstName, lastName, additionalName, profilePicture, backgroundPicture, title,
                   location, profession, seekingOpportunity)
VALUES ('user1@example.com', 'password1', 'John', 'Doe', NULL, 'profile1.jpg', 'background1.jpg',
        'Software Engineer', 'New York', 'Software Developer', 'Yes'),
       ('user2@example.com', 'password2', 'Jane', 'Smith', NULL, 'profile2.jpg', 'background2.jpg',
        'Data Scientist', 'San Francisco', 'Data Scientist', 'No'),
       ('user3@example.com', 'password3', 'Alice', 'Johnson', NULL, 'profile3.jpg', 'background3.jpg',
        'Product Manager', 'London', 'Product Manager', 'Yes'),
       ('user4@example.com', 'password4', 'Michael', 'Brown', NULL, 'profile4.jpg', 'background4.jpg',
        'Graphic Designer', 'Los Angeles', 'Graphic Designer', 'Yes'),
       ('user5@example.com', 'password5', 'Emily', 'Wilson', NULL, 'profile5.jpg', 'background5.jpg',
        'Marketing Manager', 'Chicago', 'Marketing Manager', 'No'),
       ('user6@example.com', 'password6', 'David', 'Lee', NULL, 'profile6.jpg', 'background6.jpg',
        'Financial Analyst', 'Toronto', 'Financial Analyst', 'Yes'),
       ('user7@example.com', 'password7', 'Sophia', 'Martinez', NULL, 'profile7.jpg', 'background7.jpg',
        'UX/UI Designer', 'Seattle', 'Designer', 'Yes'),
       ('user8@example.com', 'password8', 'Matthew', 'Garcia', NULL, 'profile8.jpg', 'background8.jpg',
        'Software Developer', 'Austin', 'Software Engineer', 'No'),
       ('user9@example.com', 'password9', 'Olivia', 'Lopez', NULL, 'profile9.jpg', 'background9.jpg',
        'Project Manager', 'Sydney', 'Project Manager', 'Yes'),
       ('user10@example.com', 'password10', 'Ethan', 'Nguyen', NULL, 'profile10.jpg', 'background10.jpg',
        'Software Engineer', 'San Diego', 'Software Engineer', 'No'),
       ('user11@example.com', 'password11', 'Isabella', 'Wang', NULL, 'profile11.jpg', 'background11.jpg',
        'Data Analyst', 'Vancouver', 'Data Analyst', 'Yes'),
       ('user12@example.com', 'password12', 'Liam', 'Gonzalez', NULL, 'profile12.jpg', 'background12.jpg',
        'Content Writer', 'Miami', 'Writer', 'No'),
       ('user13@example.com', 'password13', 'Ava', 'Patel', NULL, 'profile13.jpg', 'background13.jpg',
        'Marketing Specialist', 'Dallas', 'Marketing Specialist', 'Yes'),
       ('user14@example.com', 'password14', 'Mason', 'Singh', NULL, 'profile14.jpg', 'background14.jpg',
        'Frontend Developer', 'Berlin', 'Web Developer', 'Yes'),
       ('user15@example.com', 'password15', 'Sophie', 'Kim', NULL, 'profile15.jpg', 'background15.jpg',
        'Data Scientist', 'Seoul', 'Data Scientist', 'No'),
       ('user16@example.com', 'password16', 'Jackson', 'Chen', NULL, 'profile16.jpg', 'background16.jpg',
        'Business Analyst', 'Tokyo', 'Business Analyst', 'Yes'),
       ('user17@example.com', 'password17', 'Charlotte', 'Hernandez', NULL, 'profile17.jpg', 'background17.jpg',
        'UX/UI Designer', 'Paris', 'Designer', 'No'),
       ('user18@example.com', 'password18', 'James', 'Ng', NULL, 'profile18.jpg', 'background18.jpg',
        'Software Developer', 'Singapore', 'Software Engineer', 'Yes'),
       ('user19@example.com', 'password19', 'Harper', 'Liu', NULL, 'profile19.jpg', 'background19.jpg',
        'Project Manager', 'Mumbai', 'Project Manager', 'No');

