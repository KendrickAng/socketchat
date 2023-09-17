# Socketchat

Simple chat program using java sockets.

![image](https://github.com/KendrickAng/socketchat/assets/38814428/05b3881f-0d64-4ef3-8954-393d5e0c53e6)

## Quickstart

First, build the project.

```bash
make build
```

In one terminal, start the server.

```bash
java -cp out Main --server=true --port=9090 --log-level=ALL
```

**In another terminal**, start the client.

```bash
java -cp out Main --client=true --port=9090 --log-level=ALL
```

You may start as many clients as you want, as long as all their ports are the same.
