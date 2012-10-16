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
  (let [x (blobber.password/add-user "alice" "secret" (password-file))]
    (is (= (class x) java.lang.String))
    (is (= 2 (count (str/split x #":"))))
    (is (= "alice" (first (str/split x #":"))))))

(deftest add-existing-user-should-fail-test
  (let [file (password-file)]
    (blobber.password/add-user "alice" "secret" file)
    (is (= (str "Cannot add existing user \"alice\" to password file \"" file "\".")
           (try (blobber.password/add-user "alice" "fish" file)
                                  (catch Throwable e
                                    (.getMessage e)))))))

(deftest remove-user-test
  (let [file (password-file)]
    (blobber.password/add-user "alice" "secret" file)
    (blobber.password/add-user "bob" "tangent" file)
    (blobber.password/remove-user "alice" file)
    (is (not (check-user-password "alice" "secret" file)))
    (is (check-user-password "bob" "tangent" file))))

(deftest remove-non-existent-user-should-fail-test
  (let [file (password-file)]
    (is (= (str "Cannot remove non-existent user \"alice\" from password file \"" file "\".")
           (try (blobber.password/remove-user "alice" file)
                                  (catch Throwable e
                                    (.getMessage e)))))))
(deftest check-user-password-test
  (let [file (password-file)]
    (blobber.password/add-user "alice" "secret" file)
    (is (check-user-password "alice" "secret" file))
    (is (not (check-user-password "alice" "Secret" file)))))
