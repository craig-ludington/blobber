(ns blobber.passwd
  (:import jBCrypt.BCrypt)
  (:import java.io.File)
  (:import java.io.RandomAccessFile)
  (:require [clojure.java.io :as io]
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

(defn- do-locked
  "Perform operation f on file with an exclusive lock.  F is a function of one argument and is called with file as that argument."
  [file f]
  (let [channel (.getChannel (RandomAccessFile. (File. (str file ".lock")) "rw"))]
     (let [lock (.lock channel)]
       (try (f file)
            (finally (do (.release lock)
                         (.close channel)))))))

(defn add-user
  "Add user USER with password PLAINTEXT to the password file FILE.  Create a new file if FILE doesn't exist. "
  [user plaintext file]
  (do-locked file
             (fn [f]
               (let [entries (try (read-password-file f)
                                  (catch java.io.FileNotFoundException e
                                    {}))]
                 (when (entries user)
                   (throw (Throwable. (str "Cannot add existing user \"" user "\" to password file \"" f "\"."))))
                 (write-password-file f (assoc entries user (hash-password plaintext)))))))

(defn remove-user
  "Remove user USER from the password file FILE."
  [user file]
  (do-locked file
             (fn [f]
               (let [entries (read-password-file f)]
                 (when (not (entries user))
                   (throw (Throwable. (str "Cannot remove non-existent user \"" user "\" from password file \"" f "\"."))))
                 (write-password-file f (remove (fn [[u p]] (= u user)) entries))))))

(defn change-user-password
  "Change the password for the user USER to the new password PLAINTEXT in the password file FILE."
  [user plaintext file]
  (do-locked "/tmp/lock"
             (fn [f]
               (remove-user user file))
               (add-user user plaintext file)))

(defn check-user-password
  "Return true if the user USER and password PLAINTEXT match an entry in the password file FILE."
  [user plaintext file]
  (do-locked file
             (fn [f]
               (check-password plaintext ((read-password-file f) user)))))
