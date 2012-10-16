(ns blobber.password-test
  (:import (java.io File))
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:use clojure.test
        blobber.password))

(defn password-file
  []
  (try (java.io.File/createTempFile "passwd" ".tmp" (io/as-file "/tmp"))
                        (catch java.io.IOException e
                          (throw (Throwable. (str e ".  Can't create temp file in /tmp."))))))

(deftest add-user-test
  (let [x (blobber.password/add-user "alice" "secret" ( password-file))]
    (is (= (class x) java.lang.String))
    (is (= 2 (count (str/split x #":"))))
    ))
