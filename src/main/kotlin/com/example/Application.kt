package com.example

import com.example.plugins.configureRouting
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    val config= HoconApplicationConfig(ConfigFactory.load())
    val tokenManager= TokenManager(config)

    install(Authentication){
        jwt {
            verifier(tokenManager.verifyJWTToken())
            realm=config.property("realm").toString()
            validate { jwtCredential ->
                if (jwtCredential.payload.getClaim("username").asString().isNotEmpty()) {
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
            }
        }
    }

    install(ContentNegotiation){
        json()
    }

    configureRouting()

}
