(ns hashcode.core
  (require [clojure.string :as str]
           [clojure.java.io :refer [reader writer]])
  (:gen-class))


(defn- parse-file
  "Reads input file into a vector"
  [filename]
  (with-open [rdr (reader filename)]
    (->> (line-seq rdr)
         (map #(re-seq #"\S" %))
         (into []))))

(defn- process-file
  "Resolves the problem of the server caches!"
  [parsed-file]
  nil)

(defn- write-results
  "Write results to the file results.out"
  [results]
  nil)

(defn -main
  "Entry point to the app"
  [& args]
  (if-let [filename (first args)]
    (-> filename
        parse-file
        process-file
        write-results)))
