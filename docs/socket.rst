==============
Socket Library
==============

Introduction
============

The dromadaire's socket library is based on OpenNS, a socket java library.

The main goal of OpenNS is to create three tunnels considered as one socket. The first tunnel is used to send data that can wait a little while, the second is for encrypted data and the third for data that cannot wait.

It's the same with socket, but it is more easy to use, and can directly be used with the dromadaire language (which of course is better than Java)

To import the library, just use ::

	> import socket
	
Server
======

To create the server, it's pretty easy, you just have to run : ::

	> serv = socket.ServerSocket(port)

where ``serv`` is the name of your server variable and ``port`` the port where the sockets will be send

Then, you need to accept client connections to send sockets with it. ::

	> serv_socket=serv.accept()
	
It will return ``false`` when there isn't any connections, and a socket with the latest client connection.

Client
======

To create a client, simply run ::

	> client=socket.Socket(ip,port)
	
where ``client`` is the name of the client variable, ``ip`` the ip and ``port`` the port

Send datas
==========

With socket, a client can send datas of three type :

1. Normal datas

Normal datas can just simply be send with ::

	> client.write(datas)

2. Encrypted datas

Encrypted datas can be send with ::

	> client.writeSecure(datas)
	
3. Important datas

Important datas can be send with ::
	
	> client.writeFallback(datas)
	
Receive datas
=============

1. Normal datas

Normal datas can just simply be received with ::

	> serv_socket.read()

2. Encrypted datas

Encrypted datas can be received with ::

	> serv_socket.readSecure()
	
3. Important datas

Important datas can be received with ::
	
	> serv_socket.readFallback()
	
