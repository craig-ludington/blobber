(ns blobber.storage
  (:require [blobber.config :as config]
            [ clojure.java.io :as io]
            [ring.util.response :as rsp]))

(defn- uuid [] (str (java.util.UUID/randomUUID)))

(defn- relative-path [s]
  "Return the string s with a / inserted after each character."
  (apply str (interpose "/" s)))

(defn- full-path
  "Return a filesystem path starting at root for the string s."
  ([root s]
     (str root "/" (relative-path s)))
  ([s]
     (full-path config/root-directory s)))

(defn- mkdir-p [p]
  "Just like the Unix command 'mkdir -p'. True if path p was created, false if p existed."
  (io/make-parents (str p "/x")))

(defn- blob-path [key]
  "Return a Unix absolute pathname for key."
  (str (full-path key) "/blob"))

(defn- blob-url [key]
  "Return a file:// url for key."
  (str "file://" (blob-path key)))

(defn create [blob]
  "Create a new blob and return its key."
  (let [key (uuid)
        path (full-path key)]
    (when  (mkdir-p path)
      (spit (blob-url key) blob)
      key)))

(defn fetch [key]
  "Return the blob for the key."
  (try
    (slurp (blob-url key))
    (catch java.io.FileNotFoundException e
      (rsp/status (rsp/response key) 404))
    (catch java.io.IOException e
      (rsp/status (rsp/response key) 404))))

(defn delete [key]
  "Destroy the blob for the key.  There is no 'Are you sure?'."
  (try
    (do (io/delete-file (blob-path key))
        key)
    (catch java.io.FileNotFoundException e
      (rsp/status (rsp/response key) 403))
    (catch java.io.IOException e
      (rsp/status (rsp/response key) 403))))

