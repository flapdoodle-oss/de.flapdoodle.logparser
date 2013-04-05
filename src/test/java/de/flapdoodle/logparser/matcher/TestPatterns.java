package de.flapdoodle.logparser.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.collect.Lists;


public class TestPatterns {

	@Test
	public void patternsWithGroups() {
		Pattern pattern = Pattern.compile("(?<start>[a-zA-Z]+):(?<end>[a-zA-Z]+(?<number>[0-9]+))");
		Matcher matcher = pattern.matcher("halli:hallo12");
		if (matcher.find()) {
			for (int i=0;i<=matcher.groupCount();i++){
				System.out.println("Group("+i+")="+matcher.group(i));
			}
			for (String name : Lists.newArrayList("start","end","number")) {
				System.out.println("Group("+name+")="+matcher.group(name));
			}
		}
	}
}
