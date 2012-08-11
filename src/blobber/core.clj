(ns blobber.core
  (:use [compojure.core :only [defroutes GET POST DELETE ANY]]
        [ring.middleware reload])
  (:require [blobber.config :as config]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [clojure.string :as str]
            [ring.util.response]
            [ring.adapter.jetty :as ring]
            [blobber.storage :as storage]))

(defroutes routes
  (GET    "/"        []             "<h1>I'm Blobber.  Go away.</h1>")
  (GET    ["/:uuid"] [uuid]         (storage/fetch uuid))
  (DELETE ["/:uuid"] [uuid]         (storage/delete uuid))
  (POST   "/"        { body :body } (storage/create (slurp body)))
  (ANY    "*"        []             "<h1>Page not found.</h1>"))

(def application (-> (handler/site routes)
                     (wrap-reload '(blobber.core))))

(defn start []
  (ring/run-jetty #'application {:port (or config/port 8080) :join? false}))

(defn -main []
  (start))
