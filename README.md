## Before Running
MariaDB instance will be running in a docker container. 
Download [Docker Desktop](https://www.docker.com/products/docker-desktop/)
and have it running to host mariaDB instance

## To Run
1. Open Docker Desktop. Verify bottom left is showing green or "Engine Running"
2. Start up the MariaDB container in the root directory by running:
    ```
   docker compose up
   ```
   Open a new terminal to run the next few commands (to leave mariadb running without conflict)
3.  Build the project:
    ```
    ./gradlew build
    ```
    If you are seeing issues with pulling in dependencies, try running:
    ```
    ./gradlew build --refresh-dependencies
    ```
4. Run the main method in TylerApplication, or run:
    ```
   ./gradlew bootRun
   ```
5. Verify that [Shimizu](https://github.com/noahwenck/shimizu) app is [running](http://localhost:5000/ping).
6. Navigate to http://localhost:8080

