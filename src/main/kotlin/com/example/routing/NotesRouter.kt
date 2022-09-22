package com.example.routing

import com.example.db.DatabaseConnection
import com.example.db.entities.NotesEntity
import com.example.models.Note
import com.example.models.NoteRequest
import com.example.models.NoteResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Application.notesRoutes(){

    val db = DatabaseConnection.database

    routing {
        get("/notes"){
            val notes = db.from(NotesEntity)
                .select()
                .map {
                    val id= it[NotesEntity.id]
                    val note= it[NotesEntity.note]
                    Note(id?:-1,note ?:"")
                }
          call.respond(notes)
        }

        post ("/notes"){
            val request= call.receive<NoteRequest>()
            val result=db.insert(NotesEntity){
                set(it.note,request.note)
            }
            if(result==1){
                //success response
                call.respond(HttpStatusCode.OK,NoteResponse(
                    success = true,
                    data= "Value has been successfully inserted."
                ))
            }else{
                //failure response
                call.respond(HttpStatusCode.BadRequest,NoteResponse(
                    success = false,
                    data= "Failed to insert value."
                ))
            }
        }

        get("/notes/{id}"){
           val id = call.parameters["id"]?.toInt()?:-1
            val note= db.from(NotesEntity)
                .select()
                .where{NotesEntity.id.eq(id)}
                .map {
                    val id = it[NotesEntity.id]!!
                    val note=it[NotesEntity.note]!!
                    Note(id=id,note=note)
                }.firstOrNull()
             if(note==null){
                 call.respond(
                     HttpStatusCode.NotFound,
                     NoteResponse(
                         success = false,
                         data="Could not found note with id = $id"
                     )
                 )
             }else{
                 call.respond(
                     HttpStatusCode.OK,
                     NoteResponse(
                         success = true,
                         data= note
                     )
                 )
             }

        }

        put("/notes/{id}"){
            val id = call.parameters["id"]?.toInt()?:-1
            val updatedNote= call.receive<NoteRequest>()

            val rowsEffected= db.update(NotesEntity){
                set(it.note,updatedNote.note)
                where {
                    it.id.eq(id)
                }
            }
            if(rowsEffected==1){
                call.respond(
                    HttpStatusCode.OK,
                    NoteResponse(
                        success = true,
                        data = "Note has been updated"
                    )
                )
            }else{
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Note failed to update"
                    )
                )
            }
        }
    }
}