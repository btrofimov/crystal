
// This tis just saying sugar user-friendly tree representation. Please find below implicit convertor from 
trait Node2[A]
 
case class Inner[A](value:A, left : Option[Node2[A]], right : Option[Node2[A]]) extends Node2[A]
 
case class Leaf[A] (value : A) extends Node2[A]


 
// Our application 
object LetsTestSomething extends App {
 
  private def doConvert[A] (parent: Option[Node[A]], tree : Option[Node2[A]]) : Option[Node[A]] = {
    tree.map( tree1=>
      tree1 match {
        case Inner(value,left,right) => {
          val currentNode = new Node(value, parent, List.empty)
          val leftNode = doConvert(Some(currentNode),left)
          val rightNode = doConvert(Some(currentNode),right)
 
          currentNode.childs = List(leftNode, rightNode)
          currentNode
        }
        case Leaf(a) => new Node(a,parent, List(None, None))
      }
 
    )
  }
 
  implicit def convert2[A] (tree : Inner[A]) : Node[A] = doConvert(None,Some(tree)).get
  implicit def convert3[A] (tree : Node2[A]) : Option[Node2[A]] = Some(tree)
 
 
 
  val tree = Inner("1",
                   Inner("2",
                     Leaf("3"),
                     Inner("4",Leaf("5"), None)),
                   Inner("6",
                     Inner("7",None, Leaf("8")),
                     Leaf("9")
                   )
 
  )
  val iter = tree.iterator
 
  while(iter.hasNext)
    print (iter.next + " ,")
 
}
