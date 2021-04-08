=================
Natives Functions
=================

Mathematic
==========

Range
-----

The range function is used as in python for the loop

It's used as the follow : ::

	for(i:range(nb)){
		##Your code
	}
	
It can be used with :
- one argument (from 0 to this number, increasing by 1)
- two arguments (from first to second incresing by 1)
- three arguments (from first to second incresing by the third)

Round
-----

The round function is used to approxime the numbers with (or not) a given precision .

It can be used as the follow : ::

	> round(1.4235346575685)
	1
	> round(math.pi,2)
	3.14
	
Sum
---

The sum function take an array in argument, and return his sum : ::

	> sum([1,2,3]
	6.0
	
Utils
=====

Print
-----

The print function is used to print all given arguments : ::

	> print(1,"hello",[1,2,3])
	1
	'hello'
	[1, 2, 3]

Help
----

The help function gives you some help (laughing)

Input
-----

The input function will give you the text written : ::

	>a=input()
	hello
	>a
	'hello'

Map
---

The map function is used as in python : ::

	> a=[1,2,3]
	> function f(x){return x^2;}
	> map(f,a)
	[1,4,9]
	
It's the equivalent to do : ::

	> b=[]
	> for(i:a){b.add(f(i));}
	> b
	[1,4,9]

Conversion
==========

Int
---

The int function convert a string (or number) to an integer : ::

	> int("12")
	12
	> int(1.4634575685658)
	1
	
Str
---

The str function convert an integer (or array or string) to a string : ::

	> str(12)
	'12'
	> str([1,2,3])
	'[1,2,3]'

Number
------

The number function convert a string (or number) to a number : ::

	> number("12.53")
	12.53
	> number(3.14)
	3.14

Characters
==========

Chr
---

The chr function convert a int to a character : ::

	> chr(65)
	'A'
	> chr(66)
	'B'
	
Ord
---

The ord function convert a character to an integer : ::

	> ord("A")
	65
	> ord("B")
	66

File
====

The file system uses java's file system and is already implemented, it can be used like this ::

	file = File(path)

This creates a file, if it doesn't exists using ::

	file.exists()

you can create the folder with ::

	fold = File(pathWithoutEnd)
	fold.mkdir()

And then create the file with ::

	file.create()

You can read inside the file with ``file.read()``, write with ``file.write(text, append)`` and delete it with ``file.delete()``

