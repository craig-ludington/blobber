### Leiningen

Leiningen, or "Lein" is a shell script that manages installing, running, and testing Clojure.
If you have a JVM installed, Lein is the only thing you'll need.
If you don't have a JVM installed, get Java 1.7 and install it.
(Except for the Blobber code, and we'll tell you how to get that later.)
 
#### Get Lein

Follow the instructions at:
* https://github.com/technomancy/leiningen#installation

##### For the impatient:
You can just open a terminal and copy/paste the next 6 commands into the terminal.
* cd
* curl https://raw.github.com/technomancy/leiningen/preview/bin/lein
* mkdir bin
* mv lein bin
* chmod +x bin/lein
* export PATH=~/bin:$PATH

All you really need to do is get a copy of the lein shell script, make it executable,
then run

* lein self-install

Leiningen will download clojure.jar, and a few other Java libraries, and install them
in your home directory.  The subdirectories lein uses are $HOME/.lein, and $HOME/.m2,
but you don't really need to know that.

#### Test lein

If lein is on your PATH, just type

* lein repl

If lein isn't in a directory on your PATH, type

* /path/to/lein repl

You should see output something like this:

    $ lein repl
    nREPL server started on port 56096
    REPL-y 0.1.0-beta8
    Clojure 1.4.0
        Exit: Control+D or (exit) or (quit)
    Commands: (user/help)
        Docs: (doc function-name-here)
              (find-doc "part-of-name-here")
      Source: (source function-name-here)
              (user/sourcery function-name-here)
     Javadoc: (javadoc java-object-or-class-here)
    Examples from clojuredocs.org: [clojuredocs or cdoc]
              (user/clojuredocs name-here)
              (user/clojuredocs "ns-here" "name-here")
    
    user=> 

Lein started a REPL (Read-Eval-Print-Loop) for you.  A REPL is like Ruby's IRB, 
with a lot more capabilities.

The "user=>" is the REPL's prompt.

Type a Lisp expression, for example:

    user=> (+ 2 3)
    (+ 2 3)
    5

The output 5 is the result of calling the "+" function on the numbers 2 and 3.
That's pretty much what you'd expect, right?

To exit, just press Control+D and you're back at the shell prompt.

### Blobber

* git clone git@git.cashnetusa.com:red/blobber.git
* cd blobber
* lein ring server-headless

Now you have an instance of Blobber running at http://localhost:3000/.
The blobs will be in ${HOME}/blobs.  You can override that by setting BLOBBER_ROOT_DIRECTORY in the environment.

