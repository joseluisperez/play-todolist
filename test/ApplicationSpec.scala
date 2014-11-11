import play.api.test._
import play.api.test.Helpers._

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import java.util.Date
import java.text.SimpleDateFormat
import org.specs2.matcher._

import play.api.libs.json.{Json, JsValue, JsArray}
import models.Task
import models.User

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification  with JsonMatchers{

  "Controllers/Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }



//    "render the index page" in new WithApplication{
//      val home = route(FakeRequest(GET, "/")).get

//      status(home) must equalTo(OK)
//      contentType(home) must beSome.which(_ == "text/html")
//      contentAsString(home) must contain ("Your new application is ready.")
//    }
//  }


    "crear una tarea con un POST /tasks" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val size = Task.all().size
        val Some(result) = route(  
          FakeRequest(POST, "/tasks").withFormUrlEncodedBody(("label","prueba1"),("usuario","anonymous"))
          )

        status(result) must equalTo(CREATED)
        contentType(result) must beSome.which(_ == "application/json")
        //redirectLocation(result) must equalTo(Some("/tasks"))
        Task.all().size - size must equalTo(1)
        //session(result).apply("email") must equalTo("pepito@gmail.com")

        val resultJson = contentAsJson(result)
        val resultString  = Json.stringify(resultJson)
        
        resultJson match{
          case a: JsArray => a.value.length === 2
          case _ => ""
        }


        //resultString must /#(1) /("label" -> "prueba1")
        resultString must /("label" -> "prueba1")
        resultString must /("taskOwner" -> "anonymous") //pq se envia usuario y se recibe taskowner
        //resultString must /#(2) /("taskOwner" -> "anonymous") //pq se envia usuario y se recibe taskowner
        //resultString must /#(0) /("label" -> "prueba1")
        //resultString must /#(0) /("user" -> usuario)
        //resultString must /#(1) /("label" -> "Tarea2")
      }
    }


  "devolver todas las tareas en formato JSON con un GET /tasks" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

      val size1 = Task.all().size
      val taskId = Task.create("prueba1","anonymous")
      val taskId2 = Task.create("prueba2","anonymous")
      val Some(resultTask) = route(FakeRequest(GET, "/tasks"))
      Task.delete(taskId)
      Task.delete(taskId2)

      status(resultTask) must equalTo(OK)
      contentType(resultTask) must beSome.which(_ == "application/json")

      val resultJson: JsValue = contentAsJson(resultTask)
      val resultString = Json.stringify(resultJson)


      resultString must /#(2) /("label" -> "prueba1")
      resultString must /#(2) /("taskOwner" -> "anonymous")
      resultString must /#(3) /("label" -> "prueba2")
      resultString must /#(3) /("taskOwner" -> "anonymous")
      //resultString must /("id" -> taskId)
      //resultString must /("label" -> "prueba")
      //resultString must /("taskOwner" -> "anonymous")
    }
  }

    "devolver una tarea en formato JSON con un GET /tasks/<id>" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

      val taskId = Task.create("prueba","anonymous")
      val Some(resultTask) = route(FakeRequest(GET, "/tasks/"+taskId))
      Task.delete(taskId)
 
      status(resultTask) must equalTo(OK)
      contentType(resultTask) must beSome.which(_ == "application/json")
 
      val resultJson: JsValue = contentAsJson(resultTask)
      val resultString = Json.stringify(resultJson)
 
      resultString must /("id" -> taskId)
      resultString must /("label" -> "prueba")
      resultString must /("taskOwner" -> "anonymous")
      }
    }

   "eliminar una tarea con un DELETE /tasks/<id>" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

      val size1 = Task.all().size
      val taskId = Task.create("prueba","anonymous")
      val size2 = Task.all().size
      val Some(resultTask) = route(FakeRequest(DELETE, "/tasks/"+taskId))
 
      status(resultTask) must equalTo(OK)
      size2 - size1 must equalTo(1)
      size2 - Task.all().size must equalTo(1)
      }
    }

  "devolver todas las tareas de un usuario en formato JSON con un GET /<login>/tasks" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

      val usuario = "juan.perez"
      val size1 = 0
      if (User.exists(usuario)) {
        Task.all(usuario, None).size
      }

      val taskId = Task.create("prueba1", usuario)
      val taskId2 = Task.create("prueba2",usuario)
      val Some(resultTask) = route(FakeRequest(GET, "/"+usuario+"/tasks"))
      Task.delete(taskId)
      Task.delete(taskId2)

      status(resultTask) must equalTo(OK)
      contentType(resultTask) must beSome.which(_ == "application/json")
 
      val resultJson: JsValue = contentAsJson(resultTask)
      val resultString = Json.stringify(resultJson)
 
      resultString must /#(3) /("label" -> "prueba1")
      resultString must /#(3) /("taskOwner" -> usuario)
      resultString must /#(4) /("label" -> "prueba2")
      resultString must /#(4) /("taskOwner" -> usuario)
      }
    }

    "crear una tarea para un usuario con un POST /<login>/tasks" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val usuario = "juan.perez"
        val size = Task.all(usuario, None).size
        val Some(result) = route(  
          FakeRequest(POST, "/"+usuario+"/tasks").withFormUrlEncodedBody(("label","prueba1"),("usuario",usuario))
          )

        status(result) must equalTo(CREATED)
        contentType(result) must beSome.which(_ == "application/json")
        //redirectLocation(result) must equalTo(Some("/tasks"))
        Task.all(usuario, None).size - size must equalTo(1)
        //session(result).apply("email") must equalTo("pepito@gmail.com")

        val resultJson = contentAsJson(result)
        val resultString  = Json.stringify(resultJson)
 /*       
        resultJson match{
          case a: JsArray => a.value.length === 2
          case _ => ""
        }
*/
        resultString must /("label" -> "prueba1")
        resultString must /("taskOwner" -> usuario) //pq se envia usuario y se recibe taskowner
      }
    }

    "devolver todas las tareas de un usuario con fecha hoy en formato JSON con un GET /<login>/tasks/today" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

      val usuario = "juan.perez"
      val date = new Date
      val formatter = new SimpleDateFormat("yyyy-MM-dd")
      val dateSol = formatter.format(date)
      val size1 = 0
      if (User.exists(usuario)) {
        Task.all(usuario, Some(date)).size
      }

      val taskId = Task.create("prueba1", usuario, Some(date))
      val taskId2 = Task.create("prueba2",usuario, Some(date))
      val Some(resultTask) = route(FakeRequest(GET, "/"+usuario+"/tasks/today"))
      Task.delete(taskId)
      Task.delete(taskId2)

      status(resultTask) must equalTo(OK)
      contentType(resultTask) must beSome.which(_ == "application/json")
 
      val resultJson: JsValue = contentAsJson(resultTask)
      val resultString = Json.stringify(resultJson)

      resultString must /#(0) /("label" -> "prueba1")
      resultString must /#(0) /("taskOwner" -> usuario)
      //resultString must /#(0) /("date" -> dateSol)
      resultString must /#(1) /("label" -> "prueba2")
      resultString must /#(1) /("taskOwner" -> usuario)
      //resultString must /#(1) /("date" -> dateSol)
      }
    }

  }
}
