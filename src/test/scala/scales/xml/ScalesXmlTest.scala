package scales.xml

import org.specs2.mutable.Specification
import scalaz.syntax.applicative._
import scalaz.syntax.std.option._
import scalaz.std.string._
import scalaz._

import scales.utils._
import scales.xml.ScalesXml._
import scales.xml.xpath.XmlPathText

import scala.xml.Source


object ScalesXmlTest extends Specification {

  @SuppressWarnings(Array("org.wartremover.warts.ImplicitParameter"))
  private def childValidation(name: String)
                             (implicit p: XmlPath) =
    (p \* name).headOption.map(XmlPathText.text(_)).toSuccess(s"No $name set in: ${asString(p.tree)}")


  "Testing the core functionality of scales-xml" should {
    "succeed" in {
      implicit lazy val v = Xml11
      val inputStream = this.getClass.getClassLoader.getResourceAsStream("test.xml")
      val doc = loadXml(Source.fromInputStream(inputStream))
      val p = top(doc)

      val files = Success(p \* "VideoFiles" \* "FilesystemVideoOnDemandFile").map { implicit video =>
        video.map { implicit p =>
          val fileName = childValidation("filename")
          val duration = childValidation("duration").map(_.toInt)
          val videoCodec = childValidation("videoCodec")
          val videoBitrate = childValidation("videoBitrate").map(_.toInt)
          val audioCodec = childValidation("audioCodec")
          val audioBitrate = childValidation("audioBitrate").map(_.toInt)
          val fps = childValidation("fps").map(_.toDouble)
          val width = childValidation("width").map(_.toInt)
          val height = childValidation("height").map(_.toInt)
          val size = childValidation("size").map(_.toLong)
          val value = (
            fileName
              |@| duration
              |@| videoCodec
              |@| videoBitrate
              |@| audioCodec
              |@| audioBitrate
              |@| fps
              |@| width
              |@| height
              |@| size
            ) (FilesystemVideoOnDemandFile.apply)
          value.fold(e => sys.error(e), identity)
        }
      }

      files.fold(e => ko(e), list => list must haveLength(6))
    }
  }

  final case class FilesystemVideoOnDemandFile(filename: String,
                                               duration: Int,
                                               videoCode: String,
                                               videoBitrate: Int,
                                               audioCodec: String,
                                               audioBitrate: Int,
                                               fps: Double,
                                               width: Int,
                                               height: Int,
                                               size: Long
                                              )

}
