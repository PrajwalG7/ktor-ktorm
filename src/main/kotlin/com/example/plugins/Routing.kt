package com.example.plugins

import com.example.routing.authenticationRoutes
import com.example.routing.notesRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(){
     notesRoutes()
     authenticationRoutes()
}