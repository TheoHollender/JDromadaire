=====
Loops
=====

For syntax
==========

Base syntax
-----------

Example code::

	for(var = 0; var < n; var = var + 1) {
	    ## Your code
	}

This syntax is the base syntax in informatic, it uses first a set expression, here ``var = 0``, a comparate expression, here ``var < n``, and an advance expression at the end of each iteration, here ``var = var + 1``

It will run your code while the comparate expression returns true.

Iterator syntax
--------------- 

Example code::

	for(var:iter) {
	    ## Your code
	}


This syntax can be used when iter is for an example an array. If it is an array, it will result in looping on every object of the array.

While
-----

Example code::

 while(true) {

    ## Your code

 }


So this syntax is not implemented. Your code will run while the expression gives you a true, here ``true``.

Break / Continue
================

Break
-----

- Base Break

You can launch a base break by doing ``break``. This will break the loop in which you are

For an example, this code::

 for(i = 0; i < 5; i = i + 1) {

    if (i == 2) {

        break;

    }

    print(i);

 }


returns ``0 1``

- Multiple Breaks

As a dev, you may have already tryed to do double or triple break, but to do this, there is a syntax in Drom to do it : you can do ``break(x)`` to break x times. But you can not break with a variable, it is always a known number like 1,2,3...

This means that if you have two loops : ::

 for (i = 0; i < x; i = i + 1) {

    for (j = 0; j < y; j = j + 1) {

        break(2)

    }

 }


The ``break(2)`` will break both loops

Continue
--------

- Base Continue

You can launch a base continue by doing ``continue``. This will stop the actual iteration of the loop in which you are

For an example, this code::

 for(i = 0; i < 5; i = i + 1) {

    if (i == 2) {

        continue;

    }

    print(i);

 }


returns ``0 1 3 4``

- Multiple Continues

Like the break, you can give parameters to the continue. The first parameter is the id of the loop. For an example if you set the first parameter x to 5, this will break 4 loops and will continue the 5th. The second parameter is the number of jump, this means the count of times you will execute the advance expression in a for loop before continuing normally.

For an example, ::

 for(i = 0; i < 5; i = i + 1) {

    if (i == 2) {

        continue(1,2);

    }

    print(i);

 }

returns ``0 1 4``. Here, it has jumped two times the loop while launching ``i = i + 1``.

