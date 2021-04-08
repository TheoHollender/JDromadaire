=============
Import module
=============

Use
===

The ``import`` module is used like in Python to import some librairies.

For the moment, you can use it as the follow : ::

	> import library
	> library.a
	
But, a new feature will appeare soon : ::

	> from library import a,b,c
	
Here, we just import some components of the library, and when we use them, we don't have to specify the name of the library.

With this syntax, we can also import all components : ::

	> from library import *
	
But warning !

When two libraries have a function with the same name, and you just want to use one, if you import both functions with : ::

	> from library1 import *
	> from library2 import *
	
There will be confusion to which library the function is related

You can see the documentation about libraries `here <libraries.html>`_
