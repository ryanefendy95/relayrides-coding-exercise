build:
	javac Main.java Db.java TransactionManager.java

cli:
	java Main

clean:
	rm *.class