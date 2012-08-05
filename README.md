# blobber

Store, fetch, and delete binary large objects (BLOB) in a PostgreSQL database.

## Usage

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

Then create the schema, by running:

  lein run -m blobber.migration



## License

Copyright © 2012 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
