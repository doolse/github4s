package github4s

import github4s.GithubApiConfig._
import com.typesafe.config.{ Config, ConfigFactory }

class GithubApiConfig(hocon: Option[String] = None) {

  val config = hocon.fold(ConfigFactory.load)(ConfigFactory.parseString)

  def getInt(key: String) = sys.props.getOrElse(key, config.getInt(key))

  def getOptionalInt(
    key: String
  ) = sys.props.get(key).fold(config.getOptionalInt(key))(i ⇒ Option(i.toInt))

  def getString(key: String) = sys.props.getOrElse(key, config.getString(key))

  def getOptionalString(
    key: String
  ) = sys.props.get(key).fold(config.getOptionalString(key))(Option(_))
}

object GithubApiConfig {

  implicit class ConfigOps(val config: Config) {

    def getOptionalValue[T](path: String)(f: String ⇒ T) =
      if (config.hasPath(path)) {
        Option(f(path))
      } else {
        None
      }

    def getOptionalInt(path: String): Option[Int] = getOptionalValue(path)(config.getInt)

    def getOptionalString(path: String): Option[String] = getOptionalValue(path)(config.getString)
  }

  implicit val defaultConfig: GithubApiConfig = new GithubApiConfig
}