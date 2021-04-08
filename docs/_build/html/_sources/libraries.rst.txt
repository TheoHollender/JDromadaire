=========
Libraries
=========

Installed libraries
===================

For the moment, two libraries are installed by default : ``math`` and ``socket``

The ``math`` library is here to bring some mathematical operators such as ``sqrt``, ``cos``, ``sin``, ``tan``, ``abs``

The ``socket`` library is here to bring some socket connection utils.

You can find the documentations here : 

- Math : `here <math.html>`_

- Socket : `here <socket.html>`_

Make your own native libraries
==============================

You can also make your own native libraires in Java.

First, you need to open Eclipse, and create a new project

Then, add the jar of dromadaire to the build path

Create then a class in ``src>config`` named ``Exporter.java``

The base of this class is : ::

	package config;

	import java.util.HashMap;

	import libs.LibExporter;
	import parser.Node;
	import parser.nodes.NumberNode;

	public class Exporter implements LibExporter{

    		@Override
    		public HashMap<String, Node> exportClasses() {
        		// TODO Auto-generated method stub
        		HashMap<String,Node> hash = new HashMap();
        		hash.put("one", new NumberNode(1,-1,-1));
        		## here add your functions / variables
        		return hash;
    		}

	}

You can then create other class where your functions are defined and import them.

File libraries
==============

**This feature isn't fully implemented yet, but it will come as soon as possible**

Let's assume you wan't directly to create a library written in dromadaire.

You can create a file named : ``library.dmd`` where library is the name of your library and you just can import this library as the native libraries : 

	> import library
	

