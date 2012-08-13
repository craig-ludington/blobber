(ns blobber.storage
    #^{:author "Craig Ludington",
       :doc "Create/fetch/delete BLOBs and return HTTP responses for Ring."}
  (:require [blobber.config :as config]
            [filesystem-trie.core :as trie]
            [ring.util.response :as rsp]))

(defn create [blob]
  "Create a new blob and return its key.
    Return the key for a newly-created BLOB.
    Response/body:
      201/key  Body is the key.
      500/msg  Failed to create the BLOB."
  (let [key (trie/create config/root-directory blob)]
    (if key
      (rsp/status (rsp/response key) 201)
      (rsp/status (rsp/response "BLOB not created.") 500))))

(defn fetch [key]
  "Return the BLOB for the key.
   Response/body:
     200/blob Body is the requested BLOB.
     410/key  Failed to fetch key (assume it's GONE)."
  (or (trie/fetch config/root-directory key)
      (rsp/status (rsp/response key) 410)))

(defn delete [key]
  "Destroy the BLOB for the key.
   Response/body:
     200/key  Successfully deleted key.
     410/key  Failed to delete key (assume it's GONE)."
  (or (trie/delete config/root-directory key)
      (rsp/status (rsp/response key) 410)))

