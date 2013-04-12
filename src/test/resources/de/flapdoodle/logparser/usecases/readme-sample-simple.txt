2013-04-07 00:00:01 INFO: Everything is fine
2013-04-07 00:00:01 ERROR: Something went wrong
de.flapdoodle.BadCaseException: wo should stop right now
	at de.flapdoodle.HarmlessComponent.simpleMethod(HarmlessComponent.java:123)
	at de.flapdoodle.Main.main(Main.java:12)
Caused by: java.lang.NullPointerException: null
	at de.flapdoodle.stuff.ReachTheStarAdapter.reach(ReachTheStarAdapter.java:123456)
	at de.flapdoodle.stuff.ReachSomething.reach(ReachSomething.java:123456)
	... 64 common frames omitted
