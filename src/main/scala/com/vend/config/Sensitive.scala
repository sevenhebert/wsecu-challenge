package com.vend.config

final case class Sensitive(value: String) extends AnyVal {
  override def toString: String = "***"
}
