JFLAGS = -g
JC = javac
JVM= java
FILE=
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java
CLASSES = \
    Peer.java \
        FileInfo.java \
        Transfer.java \
	NapsterServer.java \
	Indexing.java \
        Main.java 

MAIN = Main

default: classes run

classes: $(CLASSES:.java=.class)

run: classes 
	$(JVM) $(MAIN)

clean:
	$(RM) *.class
