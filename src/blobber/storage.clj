(ns blobber.storage
  (:require [blobber.config :as config]
            [ clojure.java.io :as io]))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn relative-path [s]
  "Return the string s with a / inserted after each character."
  (apply str (interpose "/" s)))

(defn full-path
  "Return a filesystem path starting at root for the string s."
  ([root s]
     (str root "/" (relative-path s)))
  ([s]
     (full-path config/root-directory s)))

(defn mkdir-p [p]
  "Just like the Unix command 'mkdir -p'. True if path p was created, false if p existed."
  (io/make-parents (str p "/x")))

(defn create [blob]
  "Create a new blob and return its key."
  (let [key (uuid)
        path (full-path key)]
    (when  (mkdir-p path)
      (spit (str "file://" path "/blob" ) blob)
      key)))