=========
Functions
=========

Define functions
================

In Drom, you can define function with the keyword ``function`` It is used as the followed example : ::

	function f(x){
		## Your code
	}

A function can take multiple arguments : ::

	function f(x,y,z){
		## Your code
	}

Specificities function
======================

Like others programming languages, a function can be called and return arguments ::

	JDrom Console - Alpha [0.0.1]
	> function f(x){return x^2;}
	parser.nodes.FunctionNode@5cad8086
	> f(6)
	36
	
The main specificity of Drom's function, is that you don't have to specify the type of function (``void`` / ``int`` / ``long`` ... )

All functions are defined with ``function`` whether they have return arguments or not.
