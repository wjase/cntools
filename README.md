Cybernostics Pty Ltd
============================================

CNTOOLS - achieve more with less code

CNTOOLS was created to help in the Development of JoeyMail.net and the associated
kids Webstart Application.

It includes libraries for:
  SVG
  Animation - including animated SVG characters
  Resource Loading (from anywhere including within Webstart Apps)
  Screen stacks and UI (like mobile interfaces but in Swing)
  Declarative events
  Sprites
  Sounds
  and much more.

It includes a bunch of handy classes for stream processing and doing things
that you would be writing repeated chunks of code for.

It is evolving as I write the application that uses it.
Whenever I find myself copying a common bit of code, I abstract it out and make 
it a utility class.

Building the projects
=====================
I use maven to build these projects. Install maven and then type:
mvn install

From the this folder. The projects should import into eclipse(with the maven plugin) 
or netbeans as well.

Note: Sometimes the cntools-animator project needs a couple of goes because time sensitive tests fail.
If you can figure out what needs to change I'd love to know ;-)

Running the Examples
====================
To keep the code clean I've tried not to have any main methods in classes. Instead I put them in
the cntools-examples projects in a similary named package to the class being exercised.


I hope you find some or all of it useful.

Enjoy!

Jason Wraxall
Cybernostics 2012

