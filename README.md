# Spring Boot 6 Template Project

## Introduction
This project serves as a template for Spring Boot 6 applications. It provides a solid foundation for quickly starting new Spring Boot projects with pre-configured settings and best practices.

## Prerequisites
- GitHub account
- Git
- JDK 21 or later
- Maven
- IntelliJ IDEA (recommended) or any preferred IDE
- Docker account (for image publishing)

## Getting Started

### 1. Project Setup
1. In GitHub, create a new project using this template.
2. In the new project's Settings:
    - Enable 'Automatically delete head branches'
    - Enable 'Always suggest updating pull request branches'
3. Set Branch Protection Rules similar to this template project.

### 2. Local Development Setup
1. Clone the newly created project.
2. Configure Maven settings in your IDE:
    - Point to a valid `settings.xml` for GitHub dependency resolution.
    - Set local Maven repository outside of Microsoft's cloud (e.g., `C:\development\tools\maven-repo`).
3. Create a feature branch for your changes.

### 3. Project Customization
1. Search for TODOs in the project and update the following:
    - Rename `group-id` and `artifact-id` in `pom.xml`
    - Update `name` in `application.yaml`
    - Ensure all names match your GitHub project name
2. Rename packages and classes as needed.

### 4. GitHub Configuration
1. Add the following secrets in your GitHub project settings:
    - `DOCKER_USER`
    - `DOCKER_ACCESS_TOKEN`

### 5. Build and Deployment
1. Trigger a rebuild in GitHub Actions.
2. Upon successful build:
    - A Docker image will be pushed to GitHub Packages and Docker Hub.
    - Access your Docker Hub repository: https://hub.docker.com/repositories/domboeckli
    - Change the Docker Hub image visibility from private to public to unlock it.

## Additional Information
- The initial build in GitHub may fail. Follow the steps above to resolve any issues.
- Ensure all TODOs are addressed before considering the setup complete.
- For any issues or improvements, please create a GitHub issue in the project repository.

## Contributing
Contributions to improve this template are welcome. Please follow the standard GitHub flow:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request
