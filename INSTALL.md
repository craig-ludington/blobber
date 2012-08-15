	
## How to install Blobber

## Prerequisites:

### Java virtual machine

Any recent Java virtual machine (JVM) with version 1.6 or later will do.

Type this at the command line:

    $ java -version
    java version "1.6.0_33"
    Java(TM) SE Runtime Environment (build 1.6.0_33-b03-424-11M3720)
    Java HotSpot(TM) 64-Bit Server VM (build 20.8-b03-424, mixed mode)

If you see some output like the above, you're OK.
Otherwise, go to 

* https://github.com/technomancy/leiningen#installation

and install a JVM.

## Tomcat

Blobber is distributed as a WAR file suitable for deployment under Apache Tomcat.
So first you'll have to install Tomcat, then you can deploy the WAR file for Blobber
using Tomcat's web-based management program.

### Install Tomcat

Complete instructions for installing Tomcat can be found at:

* http://www.zh0ng.net/2011/09/install-and-configure-tomcat-7-under-macosx-or-linux/

#### Alternate Instructions for Installing Tomcat
If the instructions above don't work for some reason, the following is what we did,
so feel free to try it instead.

#### Download Tomcat 7.0 
Go to the Apache Tomcat downloads page and get the latest version (currently 7.0.29):

* http://tomcat.apache.org/download-70.cgi

#### Unpack Tomcat in /usr/local
* sudo bash
* cd /usr/local
* tar xzf $THE_TOMCAT_TAR_FILE         # Obviously an example ;)  -- use the real file name!
* cd /usr/local/$NEW_TOMCAT_DIRECTORY  # ditto

#### Configure Tomcat
In the config/ subdirectory of the Tomcat installation, you'll find a file named:
* tomcat-users.xml

Edit that file.  In the tomcat-users element, add a "manager-gui" rolename, and a username with a password:

    <tomcat-users>
      <role rolename="manager-gui"/>
      <user username="hacker" password="SeKriT" roles="manager-gui"/>
    </tomcat-users>

Now the user "hacker" can use the Tomcat administrative web interface and deploy Blobber.

#### Starting Tomcat
Blobber works right out of the box, but if you want to customize it, there are a couple of 
environment variables to set:
* BLOBBER_LISTEN_PORT
Blobber binds to this port and accepts HTTP requests.
Default is 8080.
FIXME!!! Under Tomcat, this probably doesn't work.  Make better instructions.  FIXME!!!

* BLOBBER_ROOT_DIRECTORY
Blobber creates its filesystem-based trie (http://en.wikipedia.org/wiki/Trie) in this directory.
Default is $HOME/blobs/.

If you put Tomcat in /usr/local/tomcat, you can start Tomcat like this:
* sudo bash
* export BLOBBER_ROOT_DIRECTORY=/var/blobs 
* /usr/local/tomcat/bin/startup.sh

N.B. Storing blobs in /var/blobs/ assumes Tomcat is running as root.
Tomcat supports running as an unprivileged user too.  We won't tell you
how to do that.

If you put Tomcat in /usr/local/tomcat, you can stop Tomcat like this:
* sudo bash
* /usr/local/tomcat/bin/shutdown.sh

### Deploying Blobber to Tomcat

Tomcat can deploy Blobber directly from Github if you give it the URL.
There's a nice web-based user interface for deploying.  
You can use the web user interface if you've set up your tomcat-users.xml (see above).

#### Download the Blobber WAR file

* https://github.com/craig-ludington/blobber/blob/master/target/blobber-0.1.0-SNAPSHOT-standalone.war

If there's a later version, use it instead -- we probably just forgot to update the URL in this document.
Remember where you put the file (on the server where you installed Tomcat)
so you can deploy it later with Tomcat, using the web interface.

#### Start up Tomcat and navigate to the "Manager"

##### Behind a firewall?
If the machine where you installed Tomcat is behind a firewall, you may 
have to do some ssh port-forwarding to get to Tomcat with your browser.

For example, if you installed on example.com and your user name there is "bob",
try ssh port-forwarding something like this:

* ssh -L 9000:example.com:8080 bob@example.com

Then you can use your local browser to access Tomcat like this:

* http://localhost:9000/

##### Log in to Tomcat
Use the user-name and password for the manager-gui rolename that you configured 
in config/tomcat-users.xml.
In our example above, that was "hacker" and "SeKriT".

After you're logged in, you'll be taken to the "Tomcat Web Application Manager" page.

##### Deploy Blobber

Earlier, you should have gotten the WAR file from
* https://github.com/craig-ludington/blobber/blob/master/target/blobber-0.1.0-SNAPSHOT-standalone.war

On the Tomcat web user interface, scroll down to the "Deploy" section.
Look for "WAR file to deploy".
There's a button "Choose file".
Use it to select the WAR file you downloaded.
Press the Deploy button.

##### Make sure Tomcat is happy
Look at the top of the Tomcat Web Application Manager page for the list of Applications.
Make sure Blobber is there, and that "Running" is "true".

You can click on the Blobber link (something like blobber-0.1.0-SNAPSHOT-standalone) and
you should see:

* I'm Blobber.



## Smoke Test
There's a quick smoke-test shell script in the blobber/ directory called 
* test-with-curl

You can execute that and look at the output to make sure everything's working.

You can also look at Blobber's filesystem storage using ordinary Unix shell commands:

* ls -l /var/blobs
* find /var/blobs
* etc. etc. etc.


