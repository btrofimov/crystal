trait MyIterator[T]{
  def hasNext : Boolean
  def next: T
}
 
 
class Node[T](
    value1 : T,
    var parent:Option[Node[T]],
    var left1:Option[Node[T]],
    var right1:Option[Node[T]]){
  
  def iterator : MyIterator[T] = new MyIteratorImpl(this)
 
  def value = value1
}
 
 
class MyIteratorImpl[T](rootNode : Node[T]) extends MyIterator[T]{
 
  var currNode : Node[T] = rootNode
  var finished : Boolean = false
 
  def hasNext : Boolean = {
    !finished
  }
 
  def next: T = {
    val ret = currNode.value
 
    if(currNode.left.isDefined) {
      currNode = currNode.left.get
    }
    else {
      if(currNode.right.isDefined) {
        currNode = currNode.right.get
      }
      else {
        val (_currNode,_finished) = updateState (currNode)
        currNode = _currNode
        finished = _finished
      }
    }
    ret
  }
 
 
  protected def updateState(_currNode : Node[T]) : Pair[Node[T], Boolean] = {
    var node = _currNode
 
    while ( node.parent.isDefined && (  !node.parent.get.right.isDefined || (node.parent.get.right.get == node)   )  )
      node = node.parent.get
 
    if(node.parent.isDefined)
      node = node.parent.get.right.get
 
    (node, (node == rootNode) )
  }
 
}
