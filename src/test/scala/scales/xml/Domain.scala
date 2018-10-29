package scales.xml

object Domain {

  final case class VideoEvent(jobId: Long)

  final case class VideoFiles(files: List[FilesystemVideoOnDemandFile])

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
