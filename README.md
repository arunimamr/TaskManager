# TaskManager
A mini project for managing tasks built on Java, Springboot, PostgreSQL and Liquibase. This supports basic CRUD operations like task creation, retrieval, updating, deleting and basic filtering with pagination.

# Tech stack
- Java 21
- Springboot 3.4.7
- PostgreSQL
- Liquibase (DB migration)
- Model Mapper (Dto and Entity mapping)
- JUnit and Mockito (testing)

  # Project Setup requisites
  - Java 21
  - Maven
  - PostgreSQL
  - IDE
 
  # Git Repository
  https://github.com/arunimamr/TaskManager.git

  # PostgreSQL
  - Create database -> task_manager_db
  - Update db configuration mentioned in the application.yml file (username, password, host, port)
 
  # Liquibase
  - Liquibase migration will run automatically once the application starts
  - db.changelog-master.xml file  -> master changelog file
  - db.changelog/migrations/00000-create-task.xml  -> script added for creating task table
 
   # Application
  - Verify application.yml
  - mvn spring-boot:run (From IntelliJ -> TaskManagerApplication.java -> run )
  - Runs on port 8080 by default -> localhost:8080
  - For running tests -> mvn test
 
  # API Endpoints
  1. POST: /tasks/save  -> Create new task
  2. GET: /tasks/{id}   -> Retrieve a task using Id
  3. PUT: /tasks/{id}   -> Update an existing task
  4. DELETE: /tasks/{id}  -> Delete a task
  5. GET: /tasks/all  -> Retrives all tasks and allows filtering based on Status
 
  # Validation
  titile : Required
  dueDate : must be a valid current or future date with format (yyyy-MM-dd)
  status : TODO, IN_PROGRESS, DONE
  
 
    
  
  


