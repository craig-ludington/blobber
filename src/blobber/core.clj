(ns blobber.core
  (:use [compojure.core :only [defroutes GET POST]])
  (:require [blobber.config :as config]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [clojure.string :as str]
            ;; [ring.util.response :as ring]
            [ring.adapter.jetty :as ring]))

(defroutes routes
  (GET  "/" [] "I'm Blobber."
        )
  (POST "/" { body :body } (str "This is what you posted:\n" (slurp body) "\nDone.\n")))

(def application (handler/site routes))



(defn start []
  (ring/run-jetty #'application {:port (or config/port 8080) :join? false}))

(defn -main []
  (start))

;; (defn create [params]
;;   (let [blob (:blob params)]
;;     (when-not (str/blank? blob)
;;       (model/create blob)))
;;   (ring/redirect "/"))

;; http://stackoverflow.com/questions/8214822/compojure-getting-the-body-from-a-post-request-from-which-the-content-type-head
;; application/x-www-form-urlencoded
;; application/data
;; curl -nd "Unknown error" -H "Content-Type: application/data" http://localhost:8080/

;; shows how to slurp the body
;; https://groups.google.com/forum/?fromgroups#!topic/compojure/PSR6wC64nsA%5B1-25%5D

