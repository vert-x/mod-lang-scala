package org.vertx.scala.core

import org.vertx.java.core.SSLSupport
import org.vertx.scala.VertxWrapper

trait WrappedSSLSupport extends VertxWrapper {
  override type InternalType <: SSLSupport[_]

  /**
   * If {@code ssl} is {@code true}, this signifies that any connections will be SSL connections.
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setSSL(ssl: Boolean): this.type = wrap(internal.setSSL(ssl))

  /**
   *
   * @return Is SSL enabled?
   */
  def isSSL(): Boolean = internal.isSSL()

  /**
   * Set the path to the SSL key store. This method should only be used in SSL mode, i.e. after {@link #setSSL(boolean)}
   * has been set to {@code true}.<p>
   * The SSL key store is a standard Java Key Store, and will contain the client certificate. Client certificates are
   * only required if the server requests client authentication.<p>
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setKeyStorePath(path: String): this.type = wrap(internal.setKeyStorePath(path))

  /**
   *
   * @return Get the key store path
   */
  def getKeyStorePath(): String = internal.getKeyStorePath()

  /**
   * Set the password for the SSL key store. This method should only be used in SSL mode, i.e. after {@link #setSSL(boolean)}
   * has been set to {@code true}.<p>
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setKeyStorePassword(pwd: String): this.type = wrap(internal.setKeyStorePassword(pwd))

  /**
   *
   * @return Get the key store password
   */
  def getKeyStorePassword(): String = internal.getKeyStorePassword()

  /**
   * Set the path to the SSL trust store. This method should only be used in SSL mode, i.e. after {@link #setSSL(boolean)}
   * has been set to {@code true}.<p>
   * The trust store is a standard Java Key Store, and should contain the certificates of any servers that the client trusts.
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setTrustStorePath(path: String): this.type = wrap(internal.setTrustStorePath(path))

  /**
   *
   * @return Get the trust store path
   */
  def getTrustStorePath(): String = internal.getTrustStorePath()

  /**
   * Set the password for the SSL trust store. This method should only be used in SSL mode, i.e. after {@link #setSSL(boolean)}
   * has been set to {@code true}.<p>
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setTrustStorePassword(pwd: String): this.type = wrap(internal.setTrustStorePassword(pwd))

  /**
   *
   * @return Get trust store password
   */
  def getTrustStorePassword(): String = internal.getTrustStorePassword()
}