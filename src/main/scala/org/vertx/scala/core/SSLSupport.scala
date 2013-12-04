package org.vertx.scala.core

import org.vertx.java.core.{ SSLSupport => JSSLSupport }
import org.vertx.scala.{Self, AsJava}

trait SSLSupport extends Self
  with AsJava {

  override type J <: JSSLSupport[_]

  /**
   * If {@code ssl} is {@code true}, this signifies that any connections will be SSL connections.
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setSSL(ssl: Boolean): this.type = wrap(asJava.setSSL(ssl))

  /**
   *
   * @return Is SSL enabled?
   */
  def isSSL(): Boolean = asJava.isSSL()

  /**
   * Set the path to the SSL key store. This method should only be used in SSL mode, i.e. after {@link #setSSL(boolean)}
   * has been set to {@code true}.<p>
   * The SSL key store is a standard Java Key Store, and will contain the client certificate. Client certificates are
   * only required if the server requests client authentication.<p>
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setKeyStorePath(path: String): this.type = wrap(asJava.setKeyStorePath(path))

  /**
   *
   * @return Get the key store path
   */
  def getKeyStorePath(): String = asJava.getKeyStorePath()

  /**
   * Set the password for the SSL key store. This method should only be used in SSL mode, i.e. after {@link #setSSL(boolean)}
   * has been set to {@code true}.<p>
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setKeyStorePassword(pwd: String): this.type = wrap(asJava.setKeyStorePassword(pwd))

  /**
   *
   * @return Get the key store password
   */
  def getKeyStorePassword(): String = asJava.getKeyStorePassword()

  /**
   * Set the path to the SSL trust store. This method should only be used in SSL mode, i.e. after {@link #setSSL(boolean)}
   * has been set to {@code true}.<p>
   * The trust store is a standard Java Key Store, and should contain the certificates of any servers that the client trusts.
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setTrustStorePath(path: String): this.type = wrap(asJava.setTrustStorePath(path))

  /**
   *
   * @return Get the trust store path
   */
  def getTrustStorePath(): String = asJava.getTrustStorePath()

  /**
   * Set the password for the SSL trust store. This method should only be used in SSL mode, i.e. after {@link #setSSL(boolean)}
   * has been set to {@code true}.<p>
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setTrustStorePassword(pwd: String): this.type = wrap(asJava.setTrustStorePassword(pwd))

  /**
   *
   * @return Get trust store password
   */
  def getTrustStorePassword(): String = asJava.getTrustStorePassword()
}