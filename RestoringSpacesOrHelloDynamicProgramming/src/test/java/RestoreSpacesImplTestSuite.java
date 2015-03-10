import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestSuite;
import static org.junit.Assert.*;

public class RestoreSpacesImplTestSuite extends TestSuite{

	RestoreSpacesImpl obj;
	
	@Before
	public void init(){
		obj = new RestoreSpacesImpl();
	}
	
	@Test
	public void testBuildingMapFromEmptyString(){
		List<Integer> act =  obj.buildMap("");
		List<Integer> exp = new ArrayList<>();
		assertEquals(exp, act);
	}
	
	@Test
	public void testBuildMapWithOneCharStringAndEmptyDictionary(){
		List<Integer> act =  obj.buildMap("a");
		List<Integer> exp = new ArrayList<>();
		assertEquals(exp, act);
	}

	@Test
	public void testBuildMapWithTwoCharStringAndEmptyDictionary(){
		List<Integer> act =  obj.buildMap("ab");
		List<Integer> exp = new ArrayList<>();
		assertEquals(exp, act);
	}
	
	@Test
	public void testBuildMapWithOneCharStringAndTheSameDictionary(){
		obj.dict.add("a");
		List<Integer> act =  obj.buildMap("a");
		List<Integer> exp = new ArrayList<Integer>(){{add(0);}};
		assertEquals(exp, act);
	}
	
	@Test
	public void testBuildMapWithTwoCharStringAndTwoWordDictionary(){
		obj.dict.add("a");
		obj.dict.add("b");
		List<Integer> act =  obj.buildMap("ab");
		List<Integer> exp = new ArrayList<Integer>(){{add(0);add(1);}};
		assertEquals(exp, act);
	}
	
	@Test
	public void testBuildMapWithWordIntersection(){
		obj.dict.add("ab");
		obj.dict.add("a");
		List<Integer> act =  obj.buildMap("ab");
		List<Integer> exp = new ArrayList<Integer>(){{add(0);}};
		assertEquals(exp, act);
	}

	@Test
	public void testBuildMapWithSimpleThreeWordSentence(){
		obj.dict.add("a");
		obj.dict.add("b");
		obj.dict.add("c");
		List<Integer> act =  obj.buildMap("abc");
		List<Integer> exp = new ArrayList<Integer>(){{add(0);add(1);add(2);}};
		assertEquals(exp, act);
	}

	@Test
	public void testBuildMapWithComplexWordIntersection(){
		obj.dict.add("ab");
		obj.dict.add("bc");
		obj.dict.add("cd");
		List<Integer> act =  obj.buildMap("abcd");
		List<Integer> exp = new ArrayList<Integer>(){{add(0);add(2);}};
		assertEquals(exp, act);
	}
	
	@Test
	public void testBuildMapWithComplexWordIntersectionWithotResolving(){
		obj.dict.add("ab");
		obj.dict.add("bc");
		obj.dict.add("cd");
		List<Integer> act =  obj.buildMap("abcde");
		List<Integer> exp = new ArrayList<Integer>();
		assertEquals(exp, act);
	}
	
	@Test
	public void testBuildMapWithComplexWordIntersection2(){
		obj.dict.add("ab");
		obj.dict.add("bc");
		obj.dict.add("cd");
		obj.dict.add("abcde");
		List<Integer> act =  obj.buildMap("abcde");
		List<Integer> exp = new ArrayList<Integer>(){{add(0);}};
		assertEquals(exp, act);
	}
	
	
	@Test
	public void testRestoreStringWithoutSpaces(){		
		String act =  obj.restoreString("abcde",new ArrayList<Integer>(){{add(0);}});		
		assertEquals("abcde", act);
	}
	
	@Test
	public void testRestoreStringWithEmptyList(){		
		String act =  obj.restoreString("abcde",new ArrayList<Integer>());		
		assertEquals("abcde", act);
	}

	@Test
	public void testRestoreStringWithSpaces(){		
		String act =  obj.restoreString("abcde",new ArrayList<Integer>(){{add(0);add(2);}});		
		assertEquals("ab cde", act);
	}
	

	
	@Test
	public void testRestoreSpacesWithOverlappingWordsOrHelloGreedyAlgorithm(){	
		obj.dict.add("ab");
		obj.dict.add("bc");
		obj.dict.add("cd");
		String act =  obj.restoreSpaces("abcde");		
		assertEquals("abcde", act);
	}

	@Test
	public void testRestoreSpacesWithOverlappingWordsOrHelloGreedyAlgorithm2(){	
		obj.dict.add("a");
		obj.dict.add("ab");
		obj.dict.add("c");
		obj.dict.add("bc");
		obj.dict.add("cd");
		String act =  obj.restoreSpaces("abcd");		
		assertEquals("ab cd", act);
	}

	@Test
	public void testrestoreSpacesComplexString(){	
		obj.dict.add("well");
		obj.dict.add("wellcome");
		obj.dict.add("to");
		obj.dict.add("hell");
		obj.dict.add("haven");
		String act =  obj.restoreSpaces("wellcometohell");		
		assertEquals("wellcome to hell", act);
	}

}
