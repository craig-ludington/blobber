(ns blobber.lock
  (:import java.io.File)
  (:import java.io.RandomAccessFile))

(defn exclusively
  "Invoke function f with inter-process locking ensured by a file lock on file-to-lock and intra-process locking ensured by a lock on object-to-lock."
  [file-to-lock object-to-lock f]
  (locking object-to-lock
    (let [channel (.getChannel (RandomAccessFile. (File. file-to-lock) "rw"))
          lock    (.lock channel)]
              (try (f)
                   (finally (do (.release lock)
                                (.close channel)))))))

