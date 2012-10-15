(defproject blobber "1.0.3"
  :description "Store, fetch, and delete binary large objects (BLOB) in PostgreSQL."
  :url "https://github.com/craig-ludington/blobber"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring/ring-jetty-adapter "1.1.1"]
                 [ring/ring-devel "1.1.1"]
                 ;; [ring.middleware.logger "0.2.2"]
                 [compojure "1.1.1"]
                 [ring-basic-authentication "1.0.1"]
                 [filesystem-trie "1.0.3"]]
  :ring {:handler blobber.core/application} )