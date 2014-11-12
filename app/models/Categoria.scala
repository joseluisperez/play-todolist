package models
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

object Categoria {

   def create(name: String): Long ={ 
      DB.withConnection { implicit c =>
         val id: Option[Long]  = 
            SQL("insert into category (name) values ({name})").on(
               'name -> name
            ).executeInsert()
         id.getOrElse(-1)
     }
   }

def create(owner: String, name: String): Long ={ 
      DB.withConnection { implicit c =>
         val id: Option[Long]  = 
            SQL("insert into task (task_owner, category) values ({name}, {owner})").on(
               'name -> name,
               'owner -> owner
            ).executeInsert()
         id.getOrElse(-1)
     }
   }   

   def exists(name: String): Boolean ={ 
      DB.withConnection { implicit c =>
         SQL("select count(*) from category where name = {name}").on(
            'name -> name).as(scalar[Long].single) == 1
      }
   }

}