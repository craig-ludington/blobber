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
So first you'll have to install Tomcat, then you can deploy the WAR file for Blobber.
Tomcat provides a variety of deployment methods, all of which should work.

We'll document the simplest method that we've found that also lets you specify
the path component of the URL for Blobber.  But first install Tomcat.

### Install Tomcat

#### Download Tomcat 7.0 
Go to the Apache Tomcat downloads page and get the latest version (currently 7.0.29):

* http://tomcat.apache.org/download-70.cgi

Under "Binary Distributions" and the "Core" section, get "tar.gz".

#### Unpack Tomcat in /usr/local
* sudo bash
* cd /usr/local
* tar xzf $THE\_TOMCAT\_TAR\_FILE         # Obviously an example ;)  -- use the real file name!
* cd /usr/local/$NEW\_TOMCAT\_DIRECTORY  # ditto

This will make a directory called

* /usr/local/apache-tomcat-7.0.29/

If you like, make a symbolic link:

* ln -s /usr/local/apache-tomcat-7.0.29/ /usr/local/tomcat

#### Configure Tomcat
In the conf/ subdirectory of the Tomcat installation, you'll find a file named:

* tomcat-users.xml

Edit that file.  In the tomcat-users element, add two roles, and a username with a password:

    <tomcat-users>
      <role rolename="manager-gui"/>
      <role rolename="manager-script"/>
      <user username="jrandomhacker" password="SeKrIt" roles="manager-gui,manager-script"/>
    </tomcat-users>

Now the user "jrandomhacker" can use the Tomcat administrative web interface and deploy Blobber.
For deployment, we'll use the "manager-script" role.
The "manager-gui" role is valuable for keeping Tomcat and Blobber running through a nice
user interface.

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

There are scripts for starting and shutting down Tomcat in the bin directory:

* bin/startup.sh
* bin/shutdown.sh

When you run bin/startup.sh, you'll see messages about Tomcat's configuration:

        tomcat # bin/startup.sh 
        Using CATALINA_BASE:   /usr/local/tomcat
        Using CATALINA_HOME:   /usr/local/tomcat
        Using CATALINA_TMPDIR: /usr/local/tomcat/temp
        Using JRE_HOME:        /Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home
        Using CLASSPATH:       /usr/local/tomcat/bin/bootstrap.jar:/usr/local/tomcat/bin/tomcat-juli.jar

#### Check for Java 7
Look carefully at JRE\_HOME.  Make sure it is the directory where you installed Java 7.
If JRE\_HOME doesn't lead to Java 7, Blobber won't run.

If neccessary, you can force Tomcat to use the Java version you installed by
setting the environment variable JAVA\_HOME:

* export JAVA\_HOME='/path/to/java7/'

For example:

### Tomcat File Copying Deployment
#### Fetch the WAR file
* https://github.com/craig-ludington/blobber/blob/master/target/blobber-1.0.0-standalone.war

#### Copy the WAR File

Copy or move blobber-1.0.0-standalone.war to the Tomcat webapps/ directory.
The base name of the WAR file will be the path part of the URL.

For example:

* cp blobber-1.0.0-standalone.war /usr/local/tomcat/webapps/blobber_v1

Will give you a URL path fragment of "blobber_v1".


#### Restart Tomcat
=======
        tomcat # export JAVA_HOME='/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home'
        tomcat # bin/startup.sh 
        Using CATALINA_BASE:   /usr/local/tomcat
        Using CATALINA_HOME:   /usr/local/tomcat
        Using CATALINA_TMPDIR: /usr/local/tomcat/temp
        Using JRE_HOME:        /Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home
        Using CLASSPATH:       /usr/local/tomcat/bin/bootstrap.jar:/usr/local/tomcat/bin/tomcat-juli.jar
        tomcat # 
>>>>>>> 337e20595cb8132d99b5aea4f07d73e565cd528c

After Tomcat is started, use the Tomcat Manager application to confirm the Java version.

#### Tomcat Server Status and  Manager Application

When Tomcat is installed, the root of the Tomcat server has several useful links.
Visit Tomcat in your browser:

* http://localhost:8080/

You'll see three buttons on the right-hand side:

* Server Status
* Manager App
* Host Manager

Click "Server Status" and look for:

        Server Information
        Tomcat Version	JVM Version	JVM Vendor	OS Name	OS Version	OS Architecture	Hostname	IP Address
        Apache Tomcat/7.0.29	1.7.0_06-b24	Oracle Corporation	Mac OS X	10.7.4	x86_64	roshi.local	192.168.2.103

#### Configure Blobber storage

By default, Blobber stores its data in $HOME/blobs.  That's probably not where you want it.
You can override that by setting an environment variable:

* BLOBBER\_ROOT\_DIRECTORY

For example:

        export BLOBBER_ROOT_DIRECTORY=/var/blobs
        export JAVA_HOME=/usr/local/java
        /usr/local/tomcat/bin/startup.sh

##### Make sure Tomcat is happy
Look at the top of the Tomcat Web Application Manager page for the list of Applications.
Make sure Blobber is there, and that "Running" is "true".

Click on the Blobber link (/blobber) and you should see:

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
