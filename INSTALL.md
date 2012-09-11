	
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
Blobber works right out of the box, but if you want to customize it, there are is an
environment variable to set:

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

* https://github.com/craig-ludington/blobber/blob/master/target/blobber-1.0.0-standalone.war

If there's a later version, use it instead -- we probably just forgot to update the URL in this document.
Remember where you put the file (on the server where you installed Tomcat)
so you can deploy it later with Tomcat, using the web interface.

### Tomcat File Copying Deployment
#### Copy the WAR File
Copy or move blobber-1.0.0-standalone.war to 
#### Restart Tomcat


### Tomcat Web Deployment

##### Deploy Blobber

Earlier, you should have gotten the WAR file from
* https://github.com/craig-ludington/blobber/blob/master/target/blobber-1.0.0-standalone.war

Copy the WAR file to the Tomcat webapps/ directory and remove the webapps/blobber/ directory and all its contents.

Then restart Tomcat.

##### Make sure Tomcat is happy
Look at the top of the Tomcat Web Application Manager page for the list of Applications.
Make sure Blobber is there, and that "Running" is "true".

You can click on the Blobber link (something like blobber-0.1.0-SNAPSHOT-standalone) and
you should see:

* I'm Blobber.

##### Storage
You can look at Blobber's storage using ordinary Unix shell commands:

* ls -l /var/blobs
* find /var/blobs
* etc. etc. etc.

Blobs are in leaf directories.
The name of the file containing a blob is "blob".
There are two parallel directory hierarchies named key/ and digest/.

The key/ directory contains paths to blobs by the GUID that is given out to the client as a key.

The digest/ direcgtory contains paths to blobs based on the SHA-256 that is used internally as an identity for the blob.

Each leaf node (the file called "blob") has a link count of at least two -- the link in the key/ directory and the link in the digest/ directory.

If two identical blobs are posted, a key/ directory entry is created for each one.
Both are hard links to the same entry in the digest/ directory.
This implements reference-counted storage in the most natural way on the Unix filesystem.

* /var/blobs/key/f/e/6/2/4/b/f/4/-/f/8/6/b/-/4/3/4/b/-/9/a/f/f/-/8/3/6/3/6/0/9/9/e/7/2/2/blob
* /var/blobs/digest/0/0/d/3/1/0/7/c/b/2/9/4/a/e/0/a/f/3/5/6/2/7/f/2/7/1/4/a/8/b/e/4/a/f/9/b/a/5/4/9/4/6/c/6/f/b/9/2/9/2/e/a/5/2/6/c/d/a/2/5/7/5/6/7/blob


