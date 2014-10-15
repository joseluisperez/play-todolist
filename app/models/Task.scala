package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(id: Long, label: String)

object Task {

   val task = {
      get[Long]("id") ~
      get[String]("label") map {
         case id~label => Task(id, label)
      }
   }

   def all(): List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task").as(task *)
   }

   def getTask(id: Long): Option[Task] = DB.withConnection { implicit c =>
      SQL("select * from task where id = {id}").on(
         'id -> id
      ).as(task.singleOpt)
   }

   def create(label: String): Option[Long] = {
      DB.withConnection { implicit c =>
         SQL("insert into task (label) values ({label})").on(
           'label -> label
         ).executeInsert()
      }
   }

   def delete(id: Long): Long = {
      DB.withConnection { implicit c =>
         SQL("delete from task where id = {id}").on(
            'id -> id
         ).executeUpdate()
      }
   }

}
