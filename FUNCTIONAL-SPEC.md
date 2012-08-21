# Blobber and Blobberclient Functional Specification

## Introduction

### Blobber
Blobber is RESTful immutable storage for binary large objects (BLOBs).
Its  HTTP API provides POST, GET, and DELETE.  PUT is forbidden.

It was originally written to move BLOBS from a PostgreSQL database 
to a REST API, but it can be used for anything you'd like to store.

### BlobberClient
BlobberClient is a Blobber client library, written in Ruby.
It's been tested on Ruby 1.8.7 and Ruby 1.9.3.

Blobberclient provides a thin layer of abstraction over the HTTP API
that Blobber supports.

## Blobber Functional Specification
### Introduction
This section covers the Blobber HTTP API, describing what each of the
HTTP methods expect and return.

#### Environment
Blobber is distributed as a Java WAR suitable for running under Tomcat 7.

Blobber stores client data in ordinary Unix files, in a configurable 
directory.  The root of the Blobber storage may be specified in the environment variable BLOBBER_ROOT_DIRECTORY,  It defaults to $HOME/blobs/.

Installing Blobber is covered in a separate document (https://github.com/craig-ludington/blobber/blob/master/INSTALL.md). Essentially, it's Installing and configuring Tomcat then deploying the WAR file.  Tomcat has a web service that manages deployment of WAR files.

Blobber does not provide client authentication.  Instead, it relies on network firewall rules and the like to restrict access to specified clients.

#### HTTP API
The HTTP API doesn't require anything special to use.  It's a typical REST API.  In addition to the Blobberclient, we've also used curl, wget, and Chrome.

The URL for Blobber depends on your configuration, so for simplicity's sake we'll just use "http://example.com/blobber" as the base URL in our examples.

##### POST
###### Input
Make an HTTP connection to
* http://example.com/blobber/

and POST arbitrary data.

###### Output
If successful, the HTTP response will be
* Status 201 (created)
* Body: a key, which you can use for retrieving that blob in the future.

Keys are GUUIDs, for example,

* 3384f8ee-0c30-40e2-b9ae-d1cb22a84ae1

##### GET
###### Input
Make an HTTP connection to
* http://example.com/blobber/$KEY

Replace '$KEY' with a key you received in the body of a POST.
###### Output
If successful, the HTTP response will be
* Status 200 
* Body: the stream of bytes you previously stored for that key

If you supplied a proper key but the GET failed, the HTTP response will be

* Status 410 (gone)

If you supplied an improper key (not in GUUID format) the HTTP response will be
* Status 404

##### DELETE
Make an HTTP connection to
* http://example.com/blobber/$KEY

Replace '$KEY' with a key you received in the body of a POST.
###### Output
If successful, the HTTP response will be
* Status 200 
* Body: the key for the blob you wanted to delete

If you supplied a proper key but the DELETE failed, the HTTP response will be
* Status 410 (gone)

If you supplied an improper key (not in GUUID format) the HTTP response will be
* Status 404

##### PUT
HTTP PUT is not supported because all Blobber data is immutable.

A PUT response is the same for any other request not documented above: 404.


## BlobberClient Functional Specification
### Introduction
BlobberClient is distributed as a Ruby gem (available at https://github.com/craig-ludington/blobber-client).

Install the blobber-client-1.0.0.gem in the usual fashion and require 'blobber_client' in your Ruby source file.




