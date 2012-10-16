(ns blobber.core-test
  (:import (java.io StringReader))
  (:use clojure.test
        blobber.core
        blobber.storage))

;; Quis custodiet ipsos custodes?

;; It's hard to set an environment variable
(deftest root-directory-test
  (let [rd (#'blobber.storage/root-directory)
        env-root (System/getenv "BLOBBER_ROOT_DIRECTORY")
        env-home (System/getenv "HOME")]
    (if env-root
      (is (= rd (str env-root "/blobs/") )))
    (if env-home
      (is (= rd (str env-home "/blobs/"))))))

(deftest create-test
  (let [data "spies Juiliett Class Submarine oil Bush  Axis of Evil  Rand Corporation IRA JFK Rumsfeld"
        response (create data)]
    (is (= (response :status) 201))
    (is (re-matches  blobber.core/uuid-regexp (response :body)))))

(deftest good-fetch-test
  (let [expected    "Europol DES pink noise Abu Ghraib CNCIS North Korea Manfurov White Water spies Geraldton IMF"
        key         (:body (create (StringReader. expected)))
        response    (fetch key)
        inputstream (:body response)
        fetched     (byte-array (.available inputstream))]
    (.read inputstream fetched)
    (is (= (seq fetched) (seq (.getBytes expected))))
    (is (= 200 (response :status)))))

(deftest bad-fetch-test
  (let [response (fetch "This is NOT a key!")]
    (is (= 410 (response :status)))
    (is (= nil (response :body)))))

(deftest delete-test
    (let [data "Europol DES pink noise Abu Ghraib CNCIS North Korea Manfurov White Water spies Geraldton IMF"
          create-response (create data)
          key (create-response :body)
          ;; Good delete
          delete-response (delete key)]
      (is (= 200 (delete-response :status)))
      (is (= key (delete-response :body)))
      ;; Bad delete -- Can't delete the same key twice!
      (let [bad-delete-response (delete key)]
        (is (= 200 (delete-response :status)))
        (is (= key (delete-response :body))))
      ;; Double-check that it's actually deleted.
      (let [fetch-response (fetch key)]
        (is (= 410 (fetch-response :status)))
        (is (= nil (fetch-response :body))))))

