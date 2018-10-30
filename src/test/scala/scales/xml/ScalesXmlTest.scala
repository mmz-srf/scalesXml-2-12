package scales.xml

import java.io.StringWriter

import javax.xml.transform.{OutputKeys, Source => JavaxSource, TransformerFactory}
import javax.xml.transform.stream.StreamResult
import org.specs2.mutable.Specification
import scalaz.syntax.applicative._
import scalaz.syntax.std.option._
import scalaz.std.string._
import scalaz._
import scales.xml._
import scales.utils._
import scales.xml.ScalesXml._
import scales.xml.dsl.DslBuilder
import scales.xml.xpath.XmlPathText

import scala.xml.Source


object ScalesXmlTest  extends Specification with dsl.DslImplicits {

  lazy val transformerFactory = TransformerFactory.newInstance

  def prettyPrint(source: JavaxSource): String = {
    val transformer = transformerFactory.newTransformer
    transformer.setOutputProperty(OutputKeys.INDENT, "yes")
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
    val writer = new StringWriter()
    val result = new StreamResult(writer)
    transformer.transform(source, result)
    writer.toString
  }

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

      val files = Success(p \* "FilesystemVideoOnDemandFile").map { implicit video =>
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

      val l = files.fold(e => sys.error(e), identity)
      val serializedFiles = (l.map(f =>
        (Elem("FilesystemVideoOnDemandFile") / (
          Elem("filename") ~> f.filename,
          Elem("duration") ~> f.duration.toString,
          Elem("videoCodec") ~> f.videoCodec,
          Elem("videoBitrate") ~> f.videoBitrate.toString,
          Elem("audioCodec") ~> f.audioCodec,
          Elem("audioBitrate") ~> f.audioBitrate.toString,
          Elem("fps") ~> f.fps.toString,
          Elem("width") ~> f.width.toString,
          Elem("height") ~> f.height.toString,
          Elem("size") ~> f.size.toString
        ))
      ))
      val v1 = (Elem("VideoFiles") / serializedFiles.map(_.toTree)).toTree

      prettyPrint(doc.rootElem)
        .replaceAll("\\t","")
        .replaceAll("\\s","") should_===  prettyPrint(v1)
        .replaceAll("\\t","")
        .replaceAll("\\s","")

    }
  }

  final case class FilesystemVideoOnDemandFile(filename: String,
                                               duration: Int,
                                               videoCodec: String,
                                               videoBitrate: Int,
                                               audioCodec: String,
                                               audioBitrate: Int,
                                               fps: Double,
                                               width: Int,
                                               height: Int,
                                               size: Long
                                              )

}
