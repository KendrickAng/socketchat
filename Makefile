build:
	@javac -cp src src/Main.java -d out

run: build
	@java -cp out Main
