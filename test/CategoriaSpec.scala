import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models.Categoria

//@RunWith(classOf[JUnitRunner])
class categoriaSpec extends Specification {


  "El modelo Categoria" should {
    "crear una categoria" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Categoria.create("ocio") must equalTo(1)
      }
    }

    "crear dos categorias" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Categoria.create("ocio") must equalTo(1)
        Categoria.create("deporte") must equalTo(2)
      }
    }

    "categoria ocio no existe" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Categoria.exists("ocio") must equalTo(false)
      }
    }

    "categoria deporte existe" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Categoria.create("deporte")
        Categoria.exists("deporte") must equalTo(true)      
      }
    }

  }
}
