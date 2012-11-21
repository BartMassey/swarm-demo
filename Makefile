# Copyright © 2012 Bart Massey

JAVAC = javac
JFLAGS = 

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $*.java

SOURCES = AI.java Agent.java AgentView.java Bug.java		\
	  BugAI.java MeView.java Motile.java Playfield.java	\
	  Swarm.java Thing.java ThingView.java

AUXCLASSES = AI.class Agent.class AgentView.class Bug.class	\
	     BugAI.class MeView.class Motile.class		\
	     Playfield.class Thing.class ThingView.class

CLASSES = Swarm.class $(AUXCLASSES)

Swarm.class: $(AUXCLASSES)

doc:
	mkdir doc

javadoc: doc
	cd doc; javadoc -private -sourcepath .. \
	  `echo $(SOURCES) | sh ../fixpath.sh`

clean:
	-rm -f *.class

distclean: clean
	-rm -rf doc/*
