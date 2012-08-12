# blobber

RESTful immutable storage for binary large objects (BLOBs).
Provides HTTP POST, GET, and DELETE.  PUT is forbidden.

Originally written to move BLOBS from a PostgreSQL database 
to a REST API.  So, in addition to the REST interface,
there's a migration utility that connects directly to the PostgreSQL database
to copy the data originally.

## Usage

### Migrate data from a PostgreSQL database (optional)
Create a database "blobber".

Update src/blobber/config.clj by adding your credentials to the :admin map:

    (def db {:example {:subprotocol "postgresql"
                       :subname     "//localhost:5432/blobber"
                       :user        "jrandomhacker"
                       :password    "SeKrit"}
             :admin   {:subprotocol "postgresql"
                       :subname     "//localhost:5432/blobber"
                       :user        "superuser"
                       :password    "VerySecurePassword"}
             })

For testing, you can create the schema, by running:

  lein run -m blobber.migration

Finally, copy the data over from the PostgreSQL database, by running:
   FIXME!!!

## License

Copyright © 2012 Craig Brent Ludington

Distributed under the Eclipse Public License, the same as Clojure.
