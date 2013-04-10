/**
 * Copyright (C) 2013
 *   Michael Mosmann <michael@mosmann.de>
 *
 * with contributions from
 * 	${lic.developers}
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.logparser.stacktrace;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;


public class TestStackTrace {

	@Test(expected=IllegalArgumentException.class)
	public void notEnoughLines() {
		ExceptionAndMessage exceptionAndMessage=new ExceptionAndMessage(NullPointerException.class.getName(), Lists.newArrayList("missing something"));
		List<At> stack=Lists.newArrayList();
		StackLines stackLine=new StackLines(stack, null);
		StackTrace stackTrace=new StackTrace(ImmutableList.<String>of(exceptionAndMessage.toString()), exceptionAndMessage, ImmutableList.<StackLines>of(stackLine), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void notEnoughStack() {
		ExceptionAndMessage exceptionAndMessage=new ExceptionAndMessage(NullPointerException.class.getName(), Lists.newArrayList("missing something"));
		StackTrace stackTrace=new StackTrace(ImmutableList.<String>of(exceptionAndMessage.toString()), exceptionAndMessage, ImmutableList.<StackLines>of(), null);
	}
	
	@Test
	public void minimal() {
		ExceptionAndMessage exceptionAndMessage=new ExceptionAndMessage(NullPointerException.class.getName(), Lists.newArrayList("missing something"));
		List<At> stack=Lists.newArrayList(new At("FuuClass","find",null,null));
		StackLines stackLine=new StackLines(stack, null);
		StackTrace stackTrace=new StackTrace(ImmutableList.<String>of(exceptionAndMessage.toString(),"\tat CrazyClass.nothing()"), exceptionAndMessage, ImmutableList.<StackLines>of(stackLine), null);
		
		assertEquals(1,stackTrace.stackLines().size());
		assertEquals(2,stackTrace.source().size());
		assertEquals("FuuClass",stackTrace.rootCause().firstAt().classname());
	}
}
