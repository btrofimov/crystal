// the same definition staff
 
class Node[T](
  value1 : T,
  var parent:Option[Node[T]],
  var childs : List[Option[Node[T]]]){
  def value = value1
  def iterator : MyIterator[T] = new MyIteratorImpl(this)
}
 
trait MyIterator[T]{
  def hasNext : Boolean
  def next: T
}
 
/**
 * Improved Iterator implementation based on fucntional programming and monad operations
 * 
 */ 
class MyIteratorImpl[T](rootNode : Node[T]) extends MyIterator[T]{
 
  var state = (rootNode, false)
 
  def hasNext : Boolean = {
    !state._2
  }
 
  def next: T = {
    val ret = state._1.value
 
    state = state._1.childs
      .flatten
      .map(x=>(x,false))
      .headOption
      .getOrElse({ updateState (state._1) })
    ret
  }
 
  protected def updateState(_currNode : Node[T]) : Pair[Node[T], Boolean] = {
    val node = findTopNode(_currNode)
    val node2 = node.parent.foldRight(node)( (a,_) =>a.childs.reverse.head.get)
    (node2, (node2 == rootNode) )
  }
 
  protected def findTopNode(node : Node[T]) : Node[T]= {
    if ( node.parent.isDefined && (  !node.parent.get.childs.reverse.head.isDefined || (node.parent.get.childs.reverse.head.get == node)   )  )
      findTopNode(node.parent.get)
    else
      node
  }
}
 
