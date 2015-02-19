import com.twitter.scalding._
import cascading.pipe.joiner._

/**
* The class represents just one iteration of searching connected component algorithm.
* Somewhere outside the Job code we have to run this job iteratively until N [~20] and should check number inside "count" file.
* If it is zero then we can stop running other iterations
*/
class ConnectedComponentsOneIterationJob(args : Args) extends Job(args) {
  val vertexes = Tsv( args("vertexes"),('id,'gid)).read // by default gid is equal to id
  val edges = Tsv( args("edges"), ('id_a,'id_b) ).read
  val groups = vertexes.joinWithSmaller('id -> 'id_b, vertexes.joinWithSmaller('id -> 'id_a, edges).discard('id ).rename('gid ->'gid_a))
   .discard('id )
   .rename('gid ->'gid_b)
   .filter('gid_a, 'gid_b) {gid : (String, String) => gid._1 != gid._2 }
   .project ('gid_a, 'gid_b)
   .mapTo(('gid_a, 'gid_b) -> ('gid_a, 'gid_b)) {gid : (String, String) => max(gid._1, gid._2) -> min(gid._1, gid._2) }
  
  // if count=0 then we can stop running next iterations
  groups.groupAll { _.size }.write(Tsv("count"))
  
  val new_groups = groups.groupBy('gid_a) {_.min('gid_b)}.rename(('gid_a,'gid_b)->('source, 'target))
  val new_vertexes = vertexes.joinWithSmaller('id -> 'source, new_groups, joiner = new LeftJoin )
   .mapTo( ('id,'gid,'source,'target)->('id, 'gid)) { param:(String, String, String, String) =>
      val (id, gid, source,target) = param
      if (target != null) ( id , min( gid, target ) ) else ( id, gid )
   }
  new_vertexes.write( Tsv( args("new_vertexes") ) )
}
