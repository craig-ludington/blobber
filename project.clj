(defproject blobber "0.1.0-SNAPSHOT"
  :description "Store, fetch, and delete binary large objects (BLOB) in PostgreSQL."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [postgresql/postgresql "8.4-702.jdbc4"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [ring/ring-jetty-adapter "1.1.1"]
                 [compojure "1.1.1"]])