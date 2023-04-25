# Eksamensprojekt
Eksamensprojekt is a software project aimed at creating a project management software for easy use. It is developed by "Team Smooth" consisting of the developers: Mark, Rasmus, Jonathan & Nikolai.

### Features
The Eksamensprojekt software aims to provide the following features:

- Project creation: Users can create new projects with project details, such as name, description, start and end dates.

- Task management: Users can create and manage tasks within each project, including task details such as title, description, assignees, due dates, and status.

- Kanban board: Users can view and manage tasks in a Kanban board view, with columns representing different task statuses (e.g., To Do, In Progress, Done).

- User management: Users can create accounts, log in, and manage their profiles, including changing their password and updating their personal information.

### Technologies
The Eksamensprojekt software is built using the following technologies:

Frontend: HTML, CSS, JavaScript

Backend: Java, Spring Boot, Thymeleaf, JDBC

Database: MySQL with MySQL Workbench

Local Server: Apache Tomcat on localhost port 8080

Deployment: Render through Docker container

### Installation
To install and run the Eksamensprojekt software locally, follow these steps:

1. Clone the repository: git clone https://github.com/Rasmus2103/Eksamensprojekt.git
2. Install dependencies for backend (Java, Spring Boot, Thymeleaf, JDBC) as per your development environment.
3. Set up the MySQL database:
4. Install MySQL and MySQL Workbench on your local machine, if not already installed.
5. Create a new MySQL database for the project.
6. Update the database connection configuration in the src/main/resources/application.properties file with your MySQL database details, for example:
bash
7. Copy code

`spring.datasource.url=jdbc:mysql://localhost:3306/eksamensprojekt`

`spring.datasource.username=root`

`spring.datasource.password=mypassword`

10. Build and deploy the backend server (Java, Spring Boot):
11. Build the project using Maven or your preferred build tool.
12. Deploy the generated WAR file by pushing it to the repository.
13. Build and deploy the frontend (HTML, CSS, JavaScript):
14. Host the frontend files using a local server, or use a web browser to directly open the index.html file.
15. Open your web browser and go to http://localhost:8080 or 
https://smooth-examproject.onrender.com/ to access the Eksamensprojekt software.


### Contributing
Contributions to the Eksamensprojekt project are welcome! Please refer to the CONTRIBUTE.md file for contributing guidelines.

### Deployment
The Eksamensprojekt software is deployed on Render using a Docker container. Please refer to the deployment instructions for more details.

### License
The Eksamensprojekt software is open source and released under the MIT License.

### Contact
If you have any questions or feedback, please feel free to contact the project owner Rasmus2103 through GitHub.
