build:
	@javac -cp src src/Main.java -d out

run: build
	@java -cp out Main

run-client: build
	@java -cp out Main --client=true --port=9090 --log-level=ALL

run-server: build
	@java -cp out Main --server=true --port=9090 --log-level=ALL
