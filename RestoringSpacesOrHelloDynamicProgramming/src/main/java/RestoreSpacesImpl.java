
/**
 * Class recovers spaces for specific string. The algorithm does not respect upperCase or any other delimiters except space
 * @author borys.trofymov
 *
 */
class RestoreSpacesImpl {
	
	Set<String> dict = new HashSet<>();
	
	public void setDictionary(Set<String> dict) {
		this.dict = dict;
	}

	/**
	 *  Major API method to restore spaces.
	 * @param input string without spaces
	 * @return recovered string
	 */
	public String restoreSpaces(String input) {
		// create indexes
		List<Integer> previous = buildMap(input);
		// restore string by coresponding list of indexes
		return restoreString(input, previous);
	} 
	
	/**
	 * Method builds list of space indexes for particular string
	 * Implementation uses classic dynamic programming approach based on  O(n^2), max function is number of spaces
	 * However there are still many places where possible to tune performance, like arr.addAll(...)
	 * @param input
	 * @return list of indexes where spaces have to be placed
	 */
	protected List<Integer> buildMap(String input) {
		if (input.length() == 0)
			return Collections.emptyList();
		// cache of previous solutions
		List<List<Integer>> previous = new ArrayList<>(input.length());		
		// the first major cycle, on every step i we find solution for substring length i
		for (int i = 0; i < input.length(); i++) {
			// max criteria is number of spaces for current solution
			int max = 0;
			Integer max_j = null; 
			// the second cycle, trying to find such j where this combination is the best: 
			// [<previous best solution>,<index j>,<word in dictionary>,<index i>,...<rest of input string>]
			for (int j = 0; j<=i; j++) {
				// skip zero solutions or if word [j,..,i] is not contained in dict
				if(dict.contains(input.substring(j, i + 1)) && (j==0 || previous.get(j - 1).size() > 0)){ // skip all zero solutions
					
					int prev_max = (j==0) ? 0 : previous.get(j-1).size();
					
					if( max <= (prev_max + 1) ){
						max = prev_max + 1;
						max_j = j;
					}
				}
			}
			// build list indexes for current i-step solution
			// empty list means penalty for i-step with coresponding max value 0 , if we have not succeeded
			List<Integer> arr = new LinkedList<>();
			if (max_j != null) {
				if (max_j > 0)
					arr.addAll(previous.get(max_j - 1));
				arr.add(max_j);				
				
			}
			previous.add(arr);
		}	
		// return the solution (in this case its list of indexes) for last step for char with index (input.length-1) which is basicly the best solution for entire input stringg
		return previous.get(previous.size() - 1);
	}
	
	/**
	 * Method restores spaces for partucular string based on information from list of space indexes (previous step)
	 * @param input
	 * @param map
	 * @return
	 */
	protected String restoreString(String input, List<Integer> map) {
		if (map.size() == 0)
			return input;
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < map.size(); i++) {
			int rightBound = (map.size() > i + 1) ? map.get(i + 1) : input.length();
			
			ret.append(input.substring(map.get(i),rightBound));
			ret.append(" ");
		}		
		return ret.toString().trim();
	}
}
