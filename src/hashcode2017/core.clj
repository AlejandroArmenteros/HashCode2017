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

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (if-let [filename (first args)]
  	(println (-> filename parse-file))))
