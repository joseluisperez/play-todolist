import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models.Task

//@RunWith(classOf[JUnitRunner])
class modelSpec extends Specification {


// -- Date helpers

//def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str
// --

"El modelo Task" should {
  "Crear una tarea" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val size = Task.all().size
      Task.create("label1", "anonymous", None)
      Task.all().size - size must equalTo(1)
    }
  }

  "Obtener tarea por id OK" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val id = Task.create("label2", "anonymous", None)
      val Some(task) = Task.findById(id)
      task.label must be equalTo("label2")
      task.taskOwner must be equalTo("anonymous")
    }
  }

  "Obtener tarea por id KO" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val task = Task.findById(99999)
      task must be (None)
    }
  }


"Eliminar tarea por id OK" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

      //creamos una tarea
      val id = Task.create("label3", "anonymous", None)

      //comprobamos que la tarea se ha creado correctamente
      val Some(task) = Task.findById(id)
      task.label must be equalTo("label3")
      task.taskOwner must be equalTo("anonymous")

      //eliminamos la tarea
      val result = Task.delete(id)
      result must beTrue

      //comprobamos que que la tarea no existe
      val task2 = Task.findById(id)
      task2 must be (None)
    }
  }  

"Eliminar tarea por id KO" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val size = Task.all().size
      val result = Task.delete(9999)
      result must beFalse
      Task.all().size must equalTo(size)
    }
  }  

"Obtener todas las tareas" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val tasks = Task.all()
      val size = Task.all().size
      1 must equalTo(2)
    }
  }  

}
}
