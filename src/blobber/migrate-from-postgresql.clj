(ns blobber.migrate-from-postgresql
  (:require [blobber.config    :as config]
            [clojure.java.jdbc :as sql]))

(defn migrate-blob []
  (sql/with-connection (config/db :admin)
    ;; TODO 
    ))

(defn migrate-blobs []
  ;; TODO
  )

(defn -main []
  (print " ...") (flush)
  (migrate-blobs)
  (println " done"))
