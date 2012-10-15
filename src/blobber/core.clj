(ns blobber.core
  (:use [compojure.core :only [defroutes GET POST DELETE ANY]]
        [ring.middleware reload stacktrace]
        [ring.middleware.basic-authentication :only [wrap-basic-authentication]]
;;        [ring.middleware.logger :only [wrap-with-plaintext-logger]]
        )
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [clojure.string :as str]
            [ring.util.response]
            [ring.adapter.jetty :as ring]
            [blobber.storage :as storage]))

(def uuid-regexp #"[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")

(defn- listen-port [] (or (System/getenv "BLOBBER_LISTEN_PORT") 8080))
  
(defroutes routes
  (GET    "/"                           []             "<h1>I'm Blobber.</h1>")
  (GET    "/status"                     []             (storage/health-check))
  (GET    ["/:uuid"  :uuid uuid-regexp] [uuid]         (storage/fetch uuid))
  (GET    ["//:uuid" :uuid uuid-regexp] [uuid]         (storage/fetch uuid))
  (DELETE ["/:uuid"  :uuid uuid-regexp] [uuid]         (storage/delete uuid))
  (DELETE ["//:uuid" :uuid uuid-regexp] [uuid]         (storage/delete uuid))
  (POST   "/"                           { body :body } (storage/create body)    ;; body is org.eclipse.jetty.server.HttpInput
          )
  (ANY    "*"                           []             (route/not-found (str "<h1>Key not found.</h1>"))))

(defn authenticate
  [user password]
  (and (= user "craig")
       (= password "letmein")
       "you're good"))

(def application (-> (handler/site routes)
                     (wrap-basic-authentication authenticate)
                     (wrap-reload '(blobber.core))
                     (wrap-stacktrace)))

(defn start []
  (ring/run-jetty #'application {:port (listen-port) :join? false}))

(defn -main []
  (start))
