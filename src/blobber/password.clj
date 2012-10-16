(ns blobber.password
  (:import jBCrypt.BCrypt)
  (:import java.io.File)
  (:import java.io.RandomAccessFile)
  (:require [blobber.lock :as lock]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn- hash-password
  [plaintext]
  (jBCrypt.BCrypt/hashpw plaintext (jBCrypt.BCrypt/gensalt)))

(defn- check-password
  [plaintext hashed]
  (and plaintext hashed (jBCrypt.BCrypt/checkpw plaintext hashed)))

(defn- read-password-file
  "Return the password file as a map of hashed passwords, indexed by user."
  [file]
  (let [contents (slurp file)]
    (if (= 0 (count contents))
      {}
      (into {} (map #(str/split % #":") (str/split-lines contents))))))

(defn- make-password-entry
  [user hashed]
  (str user ":" hashed "\n"))

(defn- make-password-entries
  "Return "
  [entries]
  (apply str (mapcat (fn [[u p]] (make-password-entry u p)) entries)))

(defn- write-password-file
  [file entries]
  (let [contents (make-password-entries entries)]
    (spit file contents)
    contents))

(def ^{:private true} lock)

(defn- lock-file
  [f]
  (str f ".lock"))

(defn add-user
  "Add user USER with password PLAINTEXT to the password file FILE.  Create a new file if FILE doesn't exist. "
  [user plaintext file]
  (lock/exclusively (lock-file file)
                    lock
                    #(let [entries (try (read-password-file file)
                                         (catch java.io.FileNotFoundException e
                                           {}))]
                        (when (entries user)
                          (throw (Throwable. (str "Cannot add existing user \"" user "\" to password file \"" file "\"."))))
                        (write-password-file file (assoc entries user (hash-password plaintext))))))

(defn remove-user
  "Remove user USER from the password file FILE."
  [user file]
  (lock/exclusively (lock-file file)
                    lock
                    #(let [entries (read-password-file file)]
                        (when (not (entries user))
                          (throw (Throwable. (str "Cannot remove non-existent user \"" user "\" from password file \"" file "\"."))))
                        (write-password-file file (remove (fn [[u p]] (= u user)) entries)))))

(defn check-user-password
  "Return true if the user USER and password PLAINTEXT match an entry in the password file FILE."
  [user plaintext file]
  (lock/exclusively (lock-file file)
                    lock
                    #(check-password plaintext ((read-password-file file) user))))
