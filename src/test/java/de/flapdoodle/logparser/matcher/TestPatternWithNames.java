package de.flapdoodle.logparser.matcher;

import org.junit.Assert;
import org.junit.Test;


public class TestPatternWithNames {

	@Test
	public void joinPatterns() {
		PatternWithNames name=new PatternWithNames("[a-zA-Z\\W]+");
		IPatternWithNames firstname=PatternWithNames.namedGroup("firstname",name);
		IPatternWithNames secondname=PatternWithNames.namedGroup("secondname",name);
		
		PatternWithNames start=new PatternWithNames("^");
		PatternWithNames end=new PatternWithNames("$");
		PatternWithNames space=new PatternWithNames("\\s+");
		
		IPatternWithNames joinedPattern = PatternWithNames.join(start,firstname,space,secondname,end);
		
		Assert.assertEquals("^(?<firstname>[a-zA-Z\\W]+)\\s+(?<secondname>[a-zA-Z\\W]+)$", joinedPattern.patternAsString());
		
		String line="Hans Müller";
		
		Assert.assertTrue(line,joinedPattern.find(line));
	}
}
