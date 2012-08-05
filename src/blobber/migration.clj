(ns blobber.migration
  (:require [blobber.config    :as config]
            [clojure.java.jdbc :as sql]))

(defn create-blobs []
  (sql/with-connection (config/db :admin)
    (sql/create-table :blobs
                      [:id :serial "PRIMARY KEY"]
                      [:blob :text "NOT NULL"]
                      [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"])))

(defn -main []
  (print "Migrating database...") (flush)
  (create-blobs)
  (println " done"))
