(ns hashcode2017.core
	(require [clojure.string :as str]
           [clojure.java.io :refer [reader writer]]
  (:gen-class)))


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
  (let [[config & data] parsed-file
        [rows cols min-ingr max-size] (map read-string config)
        coords (get-coordinates rows cols)
        init-status {:slices [] :pizza data}]
    (:slices (reduce #(process-coordinate min-ingr max-size %1 %2)
                     {:slices [] :pizza data}
                      data))))

(defn- write-results
  "Write results to the file results.out"
  [results]
  (with-open [wrtr (clojure.java.io/writer "results.out")]
    (.write wrtr (str (count results)))
    (.newLine wrtr)
    (doseq [slice results]
      (.write wrtr (str/join " " slice))
      (.newLine wrtr))))

(defn -main
  "Entry point to the app"
  [& args]
  (if-let [filename (first args)]
    (-> filename
        parse-file
        process-file
        write-results)))
