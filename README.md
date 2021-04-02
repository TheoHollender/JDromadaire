# JDromadaire

The goal of this tool is to create a language which uses the great points of python but uses the the Java/C++/C syntax and which uses the tools of Java like the Socket which is easyer than python's one.

## Variables

You can define a variable without saying the type like this
name = data

For the moment, there are a few base type :
Characters, Strings, Numbers (int, float, double), Array and Dict
Which can be created using the following expressions (characters and dict can't be created for the moment)
"data", 5/5.0, [0,1]

## Functions

For the moment, you can define a function by saying

The syntax will be like this:

function name(arg0, arg1, arg2, [...] , argn) {
   Your code
}

## Classes

The classes can't be created inside the code but the implementation can be done and can be seen in the functions/file folder which creates a File class to implement the file system.

# Utils of JDromadaire

## OpenNS

The networking subsystem is a project i am currently working on which will be implemented inside JDromadaire. It is used to make easyer cryptographical tunnels and it's base system creates 3 tunnels, a default for simple data, an encrypted for secure informations and a fallback for important data that needs to faster than the queue of the 2 others.

## File

The file system uses java's file system and is already implemented, it can be used like this

file = File(path)

This creates a file, if it doesn't exists using

file.exists()

you can create the folder with 

fold = File(pathWithoutEnd)
fold.mkdir()

And then create the file with

file.create()

You can read inside the file with file.read(), write with file.write(text, append) and delete it with file.delete()

# And the name

The name comes from a team of people in which i worked during a contest. This means camel in English.
