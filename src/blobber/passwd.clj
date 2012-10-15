(ns blobber.passwd
  (:import jBCrypt.BCrypt)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn- hash-password
  [plaintext]
  (jBCrypt.BCrypt/hashpw plaintext (jBCrypt.BCrypt/gensalt)))

(defn- check-password
  [plaintext hashed]
  (jBCrypt.BCrypt/checkpw plaintext hashed))

(defn- read-password-file
  "Return the password file as a map of hashed passwords, indexed by user."
  [file]
  (into {} (map #(str/split % #":") (str/split-lines (slurp file)))))

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

(defn add-user
  "Add user USER with password PLAINTEXT to the password file FILE.  Create a new file if FILE doesn't exist. "
  [user plaintext file]
  (let [entries (try (read-password-file file)
                     (catch java.io.FileNotFoundException e
                       {}))]
    (when (entries user)
      (throw (Throwable. (str "Cannot add existing user \"" user "\" to password file \"" file "\"."))))
    (write-password-file file (assoc entries user (hash-password plaintext)))))

(defn remove-user
  "Remove user USER from the password file FILE."
  [user file]
  (let [entries (read-password-file file)]
    (when (not (entries user))
      (throw (Throwable. (str "Cannot remove non-existent user \"" user "\" from password file \"" file "\"."))))
    (write-password-file file (remove (fn [[u p]] (= u user)) entries))))

(defn change-user-password
  "Change the password for the user USER to the new password PLAINTEXT in the password file FILE."
  [user plaintext file]
  (remove-user user file)
  (add-user user plaintext file))

(defn check-user-password
  "Return true if the user USER and password PLAINTEXT match an entry in the password file FILE."
  [user plaintext file]
  (check-password plaintext ((read-password-file file) user)))
