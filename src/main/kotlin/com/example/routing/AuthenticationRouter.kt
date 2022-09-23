package com.example.routing

import com.example.db.DatabaseConnection
import com.example.db.entities.UserEntity
import com.example.models.UserCredentials
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.insert

fun Application.authenticationRoutes(){

    val db= DatabaseConnection.database
    routing {
        post("/register"){
            val userCredentials= call.receive<UserCredentials>()
            val username= userCredentials.username.lowercase()
            val password= userCredentials.hashedPassword()

            db.insert(UserEntity){
                set(it.username,username)
                set(it.password,password)

            }
            call.respondText("Values inserted")

        }
    }
}