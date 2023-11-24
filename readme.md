**_Setup_**

    * docker run -d --rm --name pg -ePOSTGRES_PASSWORD=postgres -v /home/daisy/projects/postgres-db-data:/var/lib/postgresql/data  -p 5432:5432  -t postgres:14
    * ./gradlew build -t
    * ./gradlew bootRun

3. create a registration api
takes the user object

2. add password column in User table

3. change password api
