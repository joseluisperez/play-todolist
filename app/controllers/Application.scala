package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Task
import play.api.libs.json.Json

object Application extends Controller {

   val taskForm = Form(
      "label" -> nonEmptyText
   )

  def index = Action {
    Ok(views.html.index(Task.all(), taskForm))
  }

  def tasks = Action {
    Ok(collectionToJson(Task.all()))
  }

  def getTask(id: Long) = Action {
    Task.getTask(id) match {
      case Some(task) => 
        Ok( Json.toJson(Map(
          "id" -> Json.toJson(task.id),
          "label" -> Json.toJson(task.label))))
      case None => NotFound
    }
  }

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        Created(Json.toJson(Map(
          "id" -> Json.toJson(Task.create(label)),
          "label" -> Json.toJson(label))))
      }
    )
  }


  def deleteTask(id: Long) = Action {
    Task.delete(id) match {
      case 0 => NotFound
      case _ => Redirect(routes.Application.tasks)
    }
  }

  def collectionToJson(list: List[Task]): String = {
    var x = 0
    var text:String = "["
    for(x <- 0 to list.size-1){
//      text=text+"{\"id\":"+list(x).id+",\"label\":\""+list(x).label+"\"},"
      text=text+taskToJson(list(x))+","
    }
    if(text.length>1)
      text.subSequence(0, text.length()-1)+"]"
    else
      text+"]"
  }

  def taskToJson(t: Task): String = {
    t match{
      case task =>
        "{\"id\":"+task.id+",\"label\":\""+task.label+"\"}"
      case _ => "{}"
    }
  }
}
