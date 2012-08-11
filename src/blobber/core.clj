(ns blobber.core
  (:use [compojure.core :only [defroutes GET POST]]
        [ring.middleware reload])
  (:require [blobber.config :as config]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [clojure.string :as str]
            [ring.util.response]
            [ring.adapter.jetty :as ring]
            [blobber.storage :as storage]))

(defroutes routes
  (GET  "/" [] "I'm Blobber."
        )
  (POST "/" { body :body } (storage/create (slurp body))))

(def application (-> (handler/site routes)
                     (wrap-reload '(blobber.core))))

(defn start []
  (ring/run-jetty #'application {:port (or config/port 8080) :join? false}))

(defn -main []
  (start))
