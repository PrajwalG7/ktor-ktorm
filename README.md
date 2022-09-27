## REST API with Ktor | CRUD Operations using MySQL Database

A simple REST API performing CRUD Operations with MySQL Database.

### Endpoints to perform CRUD operation.

**1. Create a Note**
*    post("/notes")

     Body:
     
     `
     { "note":"your note title"}
     `

**2. Read Note**
*    get("/notes") - returns all the notes
*    get("/notes/{id}") - get note by it's Id

**3. Update a Note by it's Id**
*    put("/notes/{id}")

     Body:

     `
     { "note":"updated note title"}
     `

**4. Delete a Note by it's Id**
*    delete("/notes/{id}")
 
### Register and Login User

**1. Register**
*    post("/register")

     Body:

     `
     { "username":"user1", "password":"user1pass"}
     `

Note: username should be greater than 3 chars and password should be greater than 6 chars.

**2. Login**
*    post("/login")

     Body:

     `
     { "username":"user1", "password":"user1pass"}
     `

Note: when user will log in, the JWT token will be sent back to user as a response.

### Verifying and protecting the endpoint (protectedEndpoint)

*    get("/protectedEndpoint")

     Body:

     `
     { "username":"user1", "password":"user1pass"}
     `

     Header:

     key - Authorization

     value - Bearer jwt_token_sent_to_user
