# Event Reservation Service
This documentation provides instructions for setting up, developing, and deploying a RESTful Spring Boot application for an event reservation service using Docker, Maven, and Heroku.

## Prerequisites
- Docker: Install Docker to run containerized environments.
  - Download and install from Docker's official [website](https://docs.docker.com/get-docker/).

### Windows Users:
1. Enable Windows Subsystem for Linux Version 2 (WSL2) and install a Linux distribution (e.g., Ubuntu 20.04) for optimal performance.
  - Follow this [guide](https://www.omgubuntu.co.uk/) to enable WSL2.
  - Ensure virtualization is enabled in your BIOS. Refer to this [guide](https://support.bluestacks.com/hc/en-us/articles/115003174386-How-to-enable-Virtualization-VT-on-Windows-10-for-BlueStacks-4) if needed.
2. Download Ubuntu 20.04 from the [Microsoft Store](https://www.microsoft.com/en-us/p/ubuntu-2004-lts/9n6svws3rx71).
3. Set Ubuntu 20.04 as the default WSL distribution by running `wsl -s Ubuntu-20.04` in an elevated [Command Prompt](https://www.howtogeek.com/194041/how-to-open-the-command-prompt-as-administrator-in-windows-8.1/) and running the following command).
4. Configure Docker to use WSL2 as described in Docker's WSL [documentation](https://docs.docker.com/docker-for-windows/wsl/).


### macOS Users:
- Increase Docker's memory allocation to at least 5GB via Docker Desktop > Preferences > Resources.

## Tools and Platform

**Git**: Ensure Git is installed and configured.

**Maven**: Used for building and managing the project.

**Heroku CLI**: Required for deployment to Heroku.

**Postman**: Optional, for manually testing RESTful APIs.

## Setup
Running Docker Containers

1. Install Docker: Follow the instructions above to install and configure Docker for your operating system.
2. Start Containers:
   - Navigate to the scripts folder in the project directory.
   - Run the appropriate script for your operating system:
     - macOS/Linux: `./start-container`
     - Windows: `start-container.bat`

## Configuring Git
The first time you run the start-container script, configure Git to streamline version control:
```
git config --global credential.helper store
git config --global push.default simple
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
git config --global core.eol lf
git config --global core.autocrlf input
```
Replace "Your Name" and "your.email@example.com" with your actual details.

## Database Setup
To set up or restore the 7dbs PostgreSQL database:
1. Open a terminal and run the appropriate attach-postgres-container script from the scripts folder:
   - macOS/Linux: `./attach-postgres-container`
   - Windows: `attach-postgres-container.bat`
2. Switch to the postgres user: `su - postgres`
3. Import the database:
   ```
   dropdb 7dbs
   createdb 7dbs
   createdb 7dbs_test
   psql -d 7dbs < /app/postgres/database/7dbs.sql
   ```
4. Verify the database:
   ```
   psql 7dbs
   SELECT * FROM events;
   ```

## Development
Attaching to Containers

1. Development Container:
   - Run the appropriate attach-container script from the scripts folder:
     - macOS/Linux: `./attach-container`
     - Windows: `attach-container.bat`

2. PostgreSQL Container:
   - Use the attach-postgres-container script as described above.

## Building and Testing
Inside the development container, use the following Maven commands:
1. Compile the application: `mvn compile`
   - If using Itellij: Enable automatic Maven reload in IntelliJ by setting Maven auto-reload to "Any changes" ([guide](https://www.jetbrains.com/help/idea/delegate-build-and-run-actions-to-maven.html#auto_reload_maven)).
2. Run tests: `mvn test -P test`
3. Package the application: `mvn package spring-boot:repackage -P test`
4. Run the application locally: `mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"`
   - The application runs until stopped with CTRL+C.
   - If using Intellij: Enable auto-build in IntelliJ for automatic reloads ([guide](https://www.jetbrains.com/help/idea/compiling-applications.html#auto-build)).

## API Testing

### Postman:
Import Postman tests from the scripts/postman folder.
- Refer to Postman’s import/export [documentation](https://learning.postman.com/docs/getting-started/importing-and-exporting-data/) or this [video](https://www.youtube.com/watch?v=FzPBDU7cB74&ab_channel=FunDooTesters) for guidance.


### Swagger UI:
Access auto-generated API documentation at [http://localhost:8080/swagger-ui/](url).



## Deployment to Heroku

#### One-Time Setup

**Authenticate with Heroku:**
1. Inside the development container, run:`heroku login`
2. Follow the provided URL to log in via a web browser.
3. Subscribe to Heroku Eco Plan:
   - Follow Heroku’s Eco Dyno Hours [guide](https://devcenter.heroku.com/articles/eco-dyno-hours) to activate the Eco plan.

**Create a Heroku Application:**
1. Run: `heroku create appname`
2. Use a unique name, e.g., cs-3250-app-username.

**Add PostgreSQL Database:**
Run: `heroku addons:create heroku-postgresql:mini`

**Configure Maven Goals:**
Run: ```heroku config:set MAVEN_CUSTOM_GOALS="clean dependency:list package spring-boot:repackage install"```

### Deploying the Application

1. Commit Changes:
   ```
   git add .
   git commit -m "Deployment to Heroku"
   ```
2. Push to Heroku: `git push heroku main`
3. Access the Application:
   - Run: `heroku open`
   - This displays the application’s URL.
4. View Logs:
   - Check logs for errors: `heroku logs`
5. (Optional) Force Rebuild:
   - Create an empty commit to trigger a rebuild:
     ```
     git commit --allow-empty -m "empty commit"
     git push heroku main
     ```
     
## Troubleshooting

**Docker Issues**: Verify Docker settings match the WSL2 configuration guide for Windows or adjust memory settings for macOS.

**Database Issues**: Ensure the 7dbs database import completes successfully and verify data presence.

**Heroku Deployment**: Check logs (heroku logs) for deployment errors and ensure all one-time setup steps are completed.

