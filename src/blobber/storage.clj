(ns blobber.storage
  #^{:author "Craig Ludington",
     :doc "Create/fetch/delete BLOBs and return HTTP responses for Ring."}
  (:import (java.io InputStream))
  (:require [clojure.java.io :as io]
            [filesystem-trie.core :as trie]
            [ring.util.response :as rsp]))

(defn- root-directory [] (or (System/getenv "BLOBBER_ROOT_DIRECTORY")
                             (str (System/getenv "HOME") "/blobs/")))

(defn create
  "Create a new BLOB and return its key.
   Return the key for a newly-created BLOB.
   Response/body:
     201/key  Body is the key.
     500/msg  Failed to create the BLOB."
  [^InputStream blob]
  (let [key (trie/create (root-directory) blob)]
    (if key
      (rsp/status (rsp/response key) 201)
      (rsp/status (rsp/response "BLOB not created.") 500))))

(defn ^InputStream fetch
  "Return the BLOB for the key.
   Response/body:
     200/blob Body is the requested BLOB.
     410/key  Failed to fetch key (assume it's GONE)."
  [key]
  (let [blob (trie/fetch (root-directory) key)
        status (if blob 200 410)]
    (rsp/status (rsp/response blob) status)))

(defn delete
  "Destroy the BLOB for the key.
   Response/body:
     200/key  Successfully deleted key.
     410/key  Failed to delete key (assume it's GONE)."
  [key]
  (let [k (trie/delete (root-directory) key)
        status (if k 200 410)]
    (rsp/status (rsp/response k) status))) 
