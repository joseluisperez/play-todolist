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

}