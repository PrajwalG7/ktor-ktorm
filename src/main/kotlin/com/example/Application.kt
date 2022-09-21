package com.example

import com.example.entities.NotesEntity
import io.ktor.server.application.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    val database= Database.connect(
        url="jdbc:mysql://localhost:3306/notes",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "1234"
    )

     //    insert
    database.insert(NotesEntity){
        set(it.note,"Learn Ktor")
    }

    //read
    var notes= database.from(NotesEntity)
        .select()
    for(row in notes){
        println("${row[NotesEntity.id]}")
    }

    //update
     database.update(NotesEntity){
         set(it.note,"Learning Ktor")
         where {
             it.id.eq(1)
         }
     }

    //delete
    database.delete(NotesEntity){
        it.id.eq(2)
    }

}
