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
import models.Categoria

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationCategoriaSpec extends Specification  with JsonMatchers{


  "Controllers/Application categoria" should {


    "crear una categoria para un usuario devuelve CREATED con POST /<login>/categories" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val usuario = "juan.perez"
        val category = "deporte"
        val Some(result) = route(  
          FakeRequest(POST, "/"+usuario+"/categories").withFormUrlEncodedBody(("category", category))
          )

        status(result) must equalTo(CREATED)
      }
    }



    "crear una tarea dentro de una categoria para un usuario con POST /<login>/categories/tasks" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val usuario = "juan.perez"
        val category = "deporte"
        val label = "task_deporte1"
        val Some(result) = route(
          FakeRequest(POST, "/"+usuario+"/"+category+"/tasks").withFormUrlEncodedBody(("label", label))
          )

        status(result) must equalTo(CREATED)
      }
    }

  }
}
