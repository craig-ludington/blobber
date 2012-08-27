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
  java version "1.7.0\_06"
  Java(TM) SE Runtime Environment (build 1.7.0\_06-b24)
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

#### Unpack Tomcat in /usr/local
* sudo bash
* cd /usr/local
* tar xzf $THE\_TOMCAT\_TAR\_FILE         # Obviously an example ;)  -- use the real file name!
* cd /usr/local/$NEW\_TOMCAT\_DIRECTORY  # ditto

#### Configure Tomcat
In the config/ subdirectory of the Tomcat installation, you'll find a file named:
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

        tomcat # export JAVA_HOME='/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home'
        tomcat # bin/startup.sh 
        Using CATALINA_BASE:   /usr/local/tomcat
        Using CATALINA_HOME:   /usr/local/tomcat
        Using CATALINA_TMPDIR: /usr/local/tomcat/temp
        Using JRE_HOME:        /Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home
        Using CLASSPATH:       /usr/local/tomcat/bin/bootstrap.jar:/usr/local/tomcat/bin/tomcat-juli.jar
        tomcat # 

After Tomcat is started, use the Tomcat Manager application to confirm the Java version.
Click "Server Status" and look for:

        Server Information
        Tomcat Version	JVM Version	JVM Vendor	OS Name	OS Version	OS Architecture	Hostname	IP Address
        Apache Tomcat/7.0.29	1.7.0_06-b24	Oracle Corporation	Mac OS X	10.7.4	x86_64	roshi.local	192.168.2.103

#### Deploy the Blobber application

##### Download the Blobber WAR file

* https://github.com/craig-ludington/blobber/blob/master/target/blobber-0.99.1-standalone.war

If there's a later version, use it instead -- we probably just forgot to update the URL in this document.
Remember where you put the file (on the server where you installed Tomcat).


##### Deploy Blobber to Tomcat
When you deploy Blobber, you'll be challenged for a username/password;
use the credentials you set up in conf/tomcat-users.xml for the "manager-script" role.

Access this URL from your browser:

*  http://localhost:8080/manager/text/deploy?path=/blobber&war=file:/Users/craigl/src/blobber/target/blobber-0.99.1-standalone.war

You should see this in response:

*  OK - Deployed application at context path /blobber

#### Configure Blobber storage

By default, Blobber stores its data in $HOME/blobs.  That's probably not where you want it.
You can override that by setting an environment variable:

* BLOBBER\_ROOT\_DIRECTORY

For example:

        export BLOBBER\_ROOT\_DIRECTORY=/var/blobs
        export JAVA\_HOME=/usr/local/java
        /usr/local/tomcat/bin/startup.sh

#### Final steps

##### Make sure Tomcat is happy
Look at the top of the Tomcat Web Application Manager page for the list of Applications.
Make sure Blobber is there, and that "Running" is "true".

Click on the Blobber link (/blobber) and you should see:

* I'm Blobber.

There's a quick smoke-test shell script that uses curl to POST, GET, and DELETE blobs.
Copy it from:
* https://github.com/craig-ludington/blobber/blob/master/test-with-curl
Then run it with:
* ./test-with-curl

The script returns 0 if everything succeeded, 1 otherwise.
Details are logged in $HOME/blobber-passs and $HOME/blobber-fail respectively.

You can also look at Blobber's storage using ordinary Unix shell commands:

* ls -l /var/blobs
* find /var/blobs
* etc. etc. etc.

Blobs are in leaf directories.
The name of the file containing a blob is "blob".
Who would have guessed?

* /var/blobs/1/1/4/6/c/4/5/5/-/8/2/b/6/-/4/5/d/b/-/b/d/3/2/-/f/c/2/4/2/7/8/c/0/8/7/9/blob
