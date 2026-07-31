package scales.xml.dsl

import scales.xml.{<, ?<, Attribute, AttributeQName, Elem, ItemOrElem, Namespace, PrefixedQName, QName, Text, XmlTree} // note cannot be in parser here


trait DslImplicits {
  implicit def fromElemToBuilder(elem: Elem): DslBuilder = <(elem)

  implicit def fromQNamePairToAttribute(pair: (PrefixedQName, String)): Attribute = Attribute(pair._1, pair._2)

  implicit def fromDslBuilderToTree(dslB: DslBuilder): XmlTree = dslB.toTree

  /**
    * Only works for elems, allows simpler definitions
    */
  implicit def fromQNameToTree(qname: QName): XmlTree = DslBuilder.q2tree(qname)

  /**
    * Serialisation and other dsl friends benefit from this
    */
  implicit def fromElemToTree(elem: Elem): XmlTree = DslBuilder.elem2tree(elem)

  /**
    * Allows direct use of text where expected
    */
  implicit def fromStringToText(value: String): Text = Text(value)

  /**
    * Only works for elems, better looking than <
    */
  implicit def fromQNameToBuilder(qname: QName): DslBuilder = <(qname)

  /**
    * matches elements and attributes based on qname only
    */
  implicit def fromQNameToQNamePimper(qname: QName): QNameMPimper = new QNameMPimper(qname)

  implicit def fromTreeToDsl(tree: XmlTree): DslBuilder = DslBuilder(tree)

  implicit def fromNSToNSMPimper(ns: Namespace): NSMPimper = new NSMPimper(ns)
}

/**
  * Add ?-> to an attributeqname
  */
final class OptionalAttribute(val name: AttributeQName) {
  def ?->(value: String): Option[Attribute] =
    Some(Attribute(name, value))

  def ?->(value: Option[String]): Option[Attribute] =
    value.map(Attribute(name, _))
}

/**
  * DslImplicits centered on the OptionalDslBuilder only
  */
trait OptionalDslBuilderImplicits {

  implicit def fromElemToOptionalBuilder(elem: Elem): OptionalDslBuilder = ?<(elem)

  /**
    * Only works for elems, better looking than <
    */
  implicit def fromQNameToOptionalBuilder(qname: QName): OptionalDslBuilder = ?<(qname)

  implicit def fromTreeToODsl(tree: XmlTree): OptionalDslBuilder = OptionalDslBuilder(tree)

  /**
    * Provides access to the ~> pimps
    */
  implicit def fromPQNameToOptionalAttribute(name: AttributeQName): OptionalAttribute =
    new OptionalAttribute(name)

  /**
    * Convenience function for adding optional subtrees
    */
  implicit def fromOptionalDslToOptionalTree(optionalDsl: OptionalDslBuilder): Option[ItemOrElem] =
    optionalDsl.toOptionalTree

}
