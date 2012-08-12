(ns blobber.test-database
  (:require [blobber.config    :as config]
            [clojure.java.jdbc :as sql]))

(defn create-blobs []
  (sql/with-connection (config/db :admin)
    (sql/create-table :blobs
                      [:id :serial "PRIMARY KEY"]
                      [:hash :varchar "NOT NULL"]
                      [:blob :text "NOT NULL"]
                      [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"])))

(defn -main []
  (print "Creating test database ...") (flush)
  (create-blobs)
  (println " done"))
