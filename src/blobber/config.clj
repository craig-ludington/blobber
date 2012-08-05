(ns blobber.config)

;; Add database configuration here.  Don't commit your configs if they have passwords.
;; The :admin is used for migration.
(def db {:example {:subprotocol "postgresql"
                   :subname     "//localhost:5432/blobber"
                   :user        "jrandomhacker"
                   :password    "SeKrit"}
         :admin   {}
         })
