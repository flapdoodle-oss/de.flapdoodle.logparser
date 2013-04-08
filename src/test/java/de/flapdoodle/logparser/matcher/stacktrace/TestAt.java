package de.flapdoodle.logparser.matcher.stacktrace;

import java.util.Map;
import java.util.regex.Matcher;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import de.flapdoodle.logparser.regex.Patterns;


public class TestAt {

	@Test
	public void lineWithoutJar() {
		Optional<At> match = At.match("\tat de.flapdoodle.logparser.usecases.TestJavaLogging.outer(TestJavaLogging.java:30)");
		assertTrue(match.isPresent());
		
		ImmutableMap<String, String> attributes = match.get().attributes();
		
		assertEquals("method", "outer",attributes.get("method"));
		assertEquals("class", "de.flapdoodle.logparser.usecases.TestJavaLogging",attributes.get("class"));
		assertEquals("file", "TestJavaLogging.java",attributes.get("file"));
		assertEquals("line", "30",attributes.get("line"));
	}

	@Test
	public void lineWithJar() {
		Optional<At> match = At.match("\tat org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:45) [bla0123.jar]");
		assertTrue(match.isPresent());
		
		ImmutableMap<String, String> attributes = match.get().attributes();
		
		assertEquals("method", "runReflectiveCall",attributes.get("method"));
		assertEquals("class", "org.junit.runners.model.FrameworkMethod$1",attributes.get("class"));
		assertEquals("file", "FrameworkMethod.java",attributes.get("file"));
		assertEquals("line", "45",attributes.get("line"));
	}
	
	@Test
	public void nativeMethod() {
		Optional<At> match = At.match("\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)");
		assertTrue(match.isPresent());
		
		ImmutableMap<String, String> attributes = match.get().attributes();
		
		assertEquals("method", "invoke0",attributes.get("method"));
		assertEquals("class", "sun.reflect.NativeMethodAccessorImpl",attributes.get("class"));
		assertNull("file", attributes.get("file"));
		assertNull("line", attributes.get("line"));
	}
}
