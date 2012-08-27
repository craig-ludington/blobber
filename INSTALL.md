	
## How to install Blobber

## Prerequisites:

### Java virtual machine

Java 7 is required because we use java.nio.files.File which is new in 1.7.
You'll need a browser (curl won't work because of the "Accept License Agreement" step.
Go to 

  http://www.oracle.com/technetwork/java/javase/downloads/java-se-jdk-7-download-432154.html

and check the "Accept License Agreement" radio button then click the link for 

  http://download.oracle.com/otn-pub/java/jdk/7/jdk-7-linux-i586.tar.gz

Now install Java 7. Good directions for installing Java on Ubuntu:

  http://alexander.holbreich.org/2011/11/java-7-on-debian/

Test the Java version
  $ java -version
  java version "1.7.0_06"
  Java(TM) SE Runtime Environment (build 1.7.0_06-b24)
  Java HotSpot(TM) Server VM (build 23.2-b09, mixed mode)

If you see some output like the above, you're OK.

## Tomcat

Blobber is distributed as a WAR file suitable for deployment under Apache Tomcat.
So first you'll have to install Tomcat, then you can deploy the WAR file for Blobber
using Tomcat's web-based management program.

### Install Tomcat

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

Tomcat can deploy the Blobber WAR file directly from Github if you give it the URL.
(A WAR file is a special archive that Tomcat understands.)
There's a nice web-based user interface for deploying.  
You can use the web user interface if you've set up your tomcat-users.xml (see above).

Tomcat also can deploy Blobber just by copying a file to the right directory and restaring Tomcat.
Use either method -- both work fine.  If you don't have a browser running where the WAR file is,
copying files may be easier.  Either way, you'll need to get a copy of the WAR file, or at least 
have its URL.

#### Download the Blobber WAR file

* https://github.com/craig-ludington/blobber/blob/master/target/blobber-0.99.1-standalone.war

If there's a later version, use it instead -- we probably just forgot to update the URL in this document.
Remember where you put the file (on the server where you installed Tomcat)
so you can deploy it later with Tomcat, using the web interface.

### Tomcat File Copying Deployment
#### Copy the WAR File
Copy or move blobber-0.99.1-standalone.war to 
#### Restart Tomcat


### Tomcat Web Deployment
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
* https://github.com/craig-ludington/blobber/blob/master/target/blobber-0.99.1-standalone.war

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
There's a quick smoke-test shell script that uses curl to POST, GET, and DELETE blobs.
Copy it from:
* https://github.com/craig-ludington/blobber/blob/master/test-with-curl
Then run it with:
* ./test-with-curl

You can look at the output to make sure everything's working.

You can also look at Blobber's storage using ordinary Unix shell commands:

* ls -l /var/blobs
* find /var/blobs
* etc. etc. etc.

Blobs are in leaf directories.
The name of the file containing a blob is "blob".
Who would have guessed?

* /var/blobs/1/1/4/6/c/4/5/5/-/8/2/b/6/-/4/5/d/b/-/b/d/3/2/-/f/c/2/4/2/7/8/c/0/8/7/9/blob


