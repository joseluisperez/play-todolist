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
    Redirect(routes.Application.tasks)
  }

  def tasks = Action {
    Ok(views.html.index(Task.all(), taskForm))
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
        Task.create(label)
        Redirect(routes.Application.tasks)
      }
    )
  }

  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

}
