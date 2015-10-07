
import scala.collection.mutable.ListBuffer
import MatrixTraveller._


/**
 * Given matrix NxM and traveller standing on left-bottom cell. Each matrix's cell contains penalty value.
 * Traveller is able to move only right and up and has to reach right-top cell following 
 * minimal route (sum of involved cells is minimal)
 * So the task is to find total penalty value for found minimal route.
 *
 * Considered below implementation uses "big O" O( (N + M - 2 ) * min(N,M) ) and consumes RAM min(N,M)
 * This algorithm uses idea of dynamic programming where optimal solution on i step depends on optimal solutions on i-1 step
 * For example below we have 5 steps, every step contains one or more cells with own optimal solution.
 * +---------------+
 * | 2 | 3 | 4 | 5 |
 * +---------------|
 * | 1 | 2 | 3 | 4 |
 * +---------------|
 * | 0 | 1 | 2 | 3 |
 * +---------------+
 *
 * The benefit of dynamic approach is
 * - polynomial complexity O( (N + M - 2 ) * min(N,M) )
 * - RAM consumption does not depend on NxM. Each i step we need to keep optimal solutions for just previous step (!). 
 *   This idea drammatically reduces RAM consumption
 *   Final consumption is about min(N,M) (this is size of max diagonal)
 */

/**
 * Helpful type aliases
 */
object MatrixTraveller {

  type Point = (Int,Int)
  type MaxScore = Int

}

/**
 * Helpful Matrix representation and its dependent object-initializer
 *
 */
case class Matrix (N: Int, M:Int, elements: Vector[Vector[Int]]) {
  def apply(i : Int) : Vector[Int] = elements(i)
  def apply(i : Point) : Int = elements(i._1)(i._2)
}

object Matrix {
  def applly(elements: Vector[Vector[Int]]): Matrix = {
    Matrix(elements.size, elements(0).size, elements)
  }
}

/**
 * Main algorithm implementation
 */
class MatrixTraveller {

  def findPath(matrix: Matrix) : (List[Point], MaxScore) = {
    import matrix._
    implicit val matrixObject = matrix

    val initPos = (N - 1, 0) // left bottom position

    var previousOptSolutions = Map[Point, MaxScore]((initPos, matrix(initPos))) // max elements  should be min(N,M)

    (1 to (N + M - 2)) foreach { step =>
      // run through all sub-diagonal elements for this step
      previousOptSolutions = takeDiagonalElements(step).map { elem =>
        val leftElem = (elem._1    , elem._2 - 1)
        val downElem = (elem._1 + 1, elem._2    )

        val minDiagElem = List((leftElem,previousOptSolutions.get(leftElem)), (downElem,previousOptSolutions.get(downElem)))
          .filter(_._2.isDefined)
          .map { el => (el._1,el._2.get) }
          .min

        (elem, minDiagElem._2 + matrix(elem) )
      }
      .toMap
    }
    // return solution for the last step - it is top-right cell
    (null,previousOptSolutions((0, M - 1)))
  }

  private def takeDiagonalElements (step: Int)(implicit matrix: Matrix): List[Point] = {
    import matrix._
    val diagSize = min(matrix.N,matrix.M)
    val ret = new ListBuffer[Point]()
    var n = if (step/N == 0) N - step- 1 else 0
    var m = if (step/N == 0) 0 else step - N + 1
    ret += ((n,m))
    while(n+1 < N && m+1 < M) {
      n +=1
      m +=1
      ret += ((n,m))
    }
    ret.toList
  }

  /**
   * override compare procedure for min call, see 75 loc above
   */
  implicit val ordering = new Ordering[(Point, MaxScore)]{
    override def compare(x: (Point, MaxScore), y: (Point, MaxScore)): Int = x._2 - y._2
  }

  private def min(xs: Int*) = xs.reduceLeft(_ min _)
  private def max(xs: Int*) = xs.reduceLeft(_ max _)
}
