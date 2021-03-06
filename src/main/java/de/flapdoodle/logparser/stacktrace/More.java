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

public class More {

	private final int _count;

	public More(String count) {
		_count = Integer.valueOf(count);
	}

	public int count() {
		return _count;
	}

	@Override
	public String toString() {
		return "\t... " + _count + " more";
	}
}
/*
 * java.lang.RuntimeException: middle
 * at de.flapdoodle.logparser.usecases.TestJavaLogging.outer(TestJavaLogging.java:30)
 * at de.flapdoodle.logparser.usecases.TestJavaLogging.stacktraceWithTwoCauses(TestJavaLogging.java:20)
 * at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
 * at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
 * at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
 * at java.lang.reflect.Method.invoke(Method.java:601)
 * at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:45)
 * at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:15)
 * at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:42)
 * at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:20)
 * at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:263)
 * at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:68)
 * at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:47)
 * at org.junit.runners.ParentRunner$3.run(ParentRunner.java:231)
 * at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:60)
 * at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:229)
 * at org.junit.runners.ParentRunner.access$000(ParentRunner.java:50)
 * at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:222)
 * at org.junit.runners.ParentRunner.run(ParentRunner.java:300)
 * at org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:50)
 * at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:38)
 * at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:467)
 * at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:683)
 * at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:390)
 * at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:197)
 * Caused by: java.lang.IllegalArgumentException: java.lang.NullPointerException: without any cause
 * at de.flapdoodle.logparser.usecases.TestJavaLogging.middle(TestJavaLogging.java:38)
 * at de.flapdoodle.logparser.usecases.TestJavaLogging.outer(TestJavaLogging.java:28)
 * ... 24 more
 * Caused by: java.lang.NullPointerException: without any cause
 * at de.flapdoodle.logparser.usecases.TestJavaLogging.inner(TestJavaLogging.java:43)
 * at de.flapdoodle.logparser.usecases.TestJavaLogging.middle(TestJavaLogging.java:36)
 * ... 25 more
 */
