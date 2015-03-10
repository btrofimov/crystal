
/**
 * Class recovers spaces for specific string. The algorithm does not respect upperCase or any other delimiters except space
 * @author borys.trofymov
 *
 */
class RestoreSpacesImpl{
	
	Set<String> dict = new HashSet<>();
	
	public void setDictionary(Set<String> dict){
		this.dict = dict;
	}

	/**
	 *  Major API method to restore spaces.
	 * @param input strign without spaces
	 * @return recovered string
	 */
	public String restoreSpaces(String input){
		List<Integer> previous = buildMap(input);
		
		return restoreString(input, previous);
	} 
	
	/**
	 * Method builds list of space indexes for particular string
	 * Implementation uses classic dynamic programming approach based on  O(n^2)
	 * However there are still many places where possible to tune performance, like arr.addAll(...)
	 * @param input
	 * @return
	 */
	protected List<Integer> buildMap(String input){
		if(input.length()==0)
			return Collections.emptyList();
		
		List<List<Integer>> previous = new ArrayList<>(input.length());		
		
		for(int i = 0;i<input.length();i++){
			int max = 0;
			
			Integer max_j = null; 
			for(int j = 0; j<=i; j++){
				if(dict.contains(input.substring(j,i+1)) && (j==0 || previous.get(j-1).size()>0)){
					
					int prev_max = j==0?0:previous.get(j-1).size();
					
					if( max <= (prev_max + 1) ){
						max = prev_max + 1;
						max_j = j;
					}
				}
			}
			List<Integer> arr = new LinkedList<>();
			if(max_j != null){
				if(max_j>0)
					arr.addAll(previous.get(max_j-1));
				arr.add(max_j);				
				
			}
			previous.add(arr);
			
		}	
		return previous.get(previous.size()-1);
	}
	
	/**
	 * Method restores spaces for partucular string based on information from list of space indexes (previous step)
	 * @param input
	 * @param map
	 * @return
	 */
	protected String restoreString(String input, List<Integer> map){
		
		if(map.size()==0)
			return input;
		
		StringBuilder ret = new StringBuilder();
		for(int i = 0;i<map.size();i++){
			int rightBound = map.size()>i+1?map.get(i+1):input.length();
			
			ret.append(input.substring(map.get(i),rightBound));
			ret.append(" ");
		}		
		return ret.toString().trim();
	}
}
