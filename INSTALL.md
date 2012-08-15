
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
** Blobber binds to this port and accepts HTTP requests.
** Default is 8080

* BLOBBER_ROOT_DIRECTORY
** Blobber creates its filesystem-based trie (http://en.wikipedia.org/wiki/Trie) in this directory.
** Default is $HOME/blobs/.

If you put Tomcat in /usr/local/tomcat, you can start Tomcat like this:
* sudo bash
* export BLOBBER_ROOT_DIRECTORY=/var/blobs 
** N.B. That assumes Tomcat is running as root.
* /usr/local/tomcat/bin/startup.sh

If you put Tomcat in /usr/local/tomcat, you can stop Tomcat like this:
* sudo bash
* /usr/local/tomcat/bin/shutdown.sh

### Deploying Blobber to Tomcat

Tomcat can deploy Blobber directly from Github if you give it the URL.
There's a nice web-based user interface for deploying.  You can use the web user interface 
if you've set up your tomcat-users.xml (see above).

Here's the Blobber WAR file URL:
* https://github.com/craig-ludington/blobber/blob/master/target/blobber-0.1.0-SNAPSHOT-standalone.war
** If there's a later version, use it instead -- we probably just forgot to update the URL in this document.

#### Maybe we can just give a github url and deploy directly from there?  TODO

TODO

## Smoke Test
There's a quick smoke-test shell script in the blobber/ directory called 
* test-with-curl

