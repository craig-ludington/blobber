(ns blobber.migrate-from-postgresql
  (:require [blobber.config    :as config]
            [clojure.java.jdbc :as sql]
            [blobber.storage   :as storage]))

(defn migrate-blob [result]
  (println (str result))
  (sql/with-connection (config/db :admin)
    (sql/transaction
     (let [key (storage/create (:blob result))]
       (sql/update-values :blobs ["id = ?" (:id result)] {:hash key :blob nil} )))))

(defn migrate-blobs []
  (sql/with-connection (config/db :admin)
    (sql/with-query-results
      results
      ["SELECT id, blob FROM blobs WHERE hash IS NULL AND blob IS NOT NULL ORDER BY id ASC LIMIT 10"]
      (doseq [result results] (migrate-blob result)))))

(defn -main []
  (print " ...") (flush)
  (migrate-blobs)
  (println " done"))
