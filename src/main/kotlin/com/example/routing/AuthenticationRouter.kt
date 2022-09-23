package com.example.routing

import com.example.db.DatabaseConnection
import com.example.db.entities.UserEntity
import com.example.models.NoteResponse
import com.example.models.User
import com.example.models.UserCredentials
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

fun Application.authenticationRoutes(){

    val db= DatabaseConnection.database
    routing {
        post("/register"){
            val userCredentials= call.receive<UserCredentials>()

            //check validation
            if(!userCredentials.isValidCredentials()){
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data="username should be greater than 3 and password should be greater than 6"
                    )
                )
                return@post
            }

            val username= userCredentials.username.lowercase()
            val password= userCredentials.hashedPassword()

            // check if username already exists
            val user= db.from(UserEntity)
                .select()
                .where { UserEntity.username.eq(username) }
                .map { it[UserEntity.username] }
                .firstOrNull()

            if (user!=null){
                call.respond(HttpStatusCode.BadRequest,
                NoteResponse(
                    success = false,
                    data = "Username already exits,Please try different username"
                ))
            }

            db.insert(UserEntity){
                set(it.username,username)
                set(it.password,password)

            }
            call.respond(HttpStatusCode.Created,NoteResponse(success = true,
            data="User has been successfully created"))

        }

        post("/login"){
            val userCredentials= call.receive<UserCredentials>()

            //check validation
            if(!userCredentials.isValidCredentials()){
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data="username should be greater than 3 and password should be greater than 6"
                    )
                )
                return@post
            }

            val username= userCredentials.username.lowercase()
            val password= userCredentials.password

            //check if user exists
            val user = db.from(UserEntity)
                .select()
                .where {
                    UserEntity.username.eq(username)
                }
                .map {
                    val id = it[UserEntity.id]!!
                    val username=it[UserEntity.username]!!
                    val password= it[UserEntity.password]!!
                    User(id,username, password)
                }.firstOrNull()

            if (user==null){
                call.respond(HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Invalid username or password."
                    ))
            }
            val doesPasswordMatch= BCrypt.checkpw(password,user?.password)
            if(!doesPasswordMatch){
                call.respond(HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Invalid username or password."
                    ))
            }
            call.respond(HttpStatusCode.BadRequest,
                NoteResponse(
                    success = true,
                    data = "User successfully logged in."
                ))
        }
    }
}