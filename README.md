

## API_Testing_REST-Assured

### Description
- Login to the collection with valid email and password and generate token
- Check invalid Login
- Get user list with valid info and user list is not found for invalid info
- Create,Search,Update and Delete user.


### Tools and Framework
- Rest-Assured
- TestNG
- Intellij
- Java

### How to run the project
#### Prerequisite
- JDK 8 or higher
- java IDE
- configure JAVA_HOME and GRADLE_HOME

#### Steps to run
- Clone the repo
- Give command in root directory
```bash
  gradle clean test 
```

#### Report
- After run the project give the following command for generate Allure Report

```bash
  allure generate allure-results --clean -o allure-report
```
    allure serve allure-results  

Report Overview
  ![Overview](https://user-images.githubusercontent.com/108132871/187906494-967d6f8b-a121-4bf5-967c-45d797fcac66.PNG)
  
Suites
  ![suite](https://user-images.githubusercontent.com/108132871/187906527-ec19a3f4-70b3-4c63-8217-9fca69549c7d.PNG)
