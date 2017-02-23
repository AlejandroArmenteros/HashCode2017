(ns hashcode2017.core
  (require [clojure.string :as str]
           [clojure.java.io :refer [reader writer]])
  (:gen-class))

(defn- ->int 
  [x]
  (read-string (str x)))

(defn- mapline
  [line]
  (map #(->int %) line))

(defn- parse-file
  "Reads input file into a vector"
  [filename]
  (with-open [rdr (reader filename)]
    (->> (line-seq rdr)
         (map (fn [itm] (mapline (re-seq #"\d+" itm))))
         ;(map (fn [itm] (mapline %) (re-seq #"\d+" itm)))
         (into []))))

(defn- calculate-latencies [caches]
  (let [h (reduce merge {} (map (fn [h] (hash-map
                                          (keyword (str (first h)))
                                          (last h)))
                                        caches))
        ret (sort-by last h)
        pret (map #(read-string (name %)) (keys ret))]
        (println "Calculated latencies:")
        (println pret)
        pret))


(defn- parse-endpoint [[[datacenter-latency caches-connected] & tail]]
  [datacenter-latency caches-connected
          (take caches-connected tail)
          (drop caches-connected tail)])

(defn- parse-endpoints [e t endpoints result] 
  (if (< e endpoints)
            (let [[datacenter-latency caches-connected caches-e t2 :as p] (parse-endpoint t)]
              (recur (inc e) t2 endpoints (assoc result e {:id e
                                                 :latency-datacenter datacenter-latency
                                                 :number-caches caches-connected
                                                 :latencies (calculate-latencies caches-e)})))))

(defn- parse-request [[video endpoint requests]]
  (println requests))

(defn- parse-requests [tail]
  (map #(parse-request %) tail))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (if-let [filename (first args)]
    (let [[[videos endpoints request-desc cache size] & [videos-size & tail]] (-> filename parse-file)]
      (parse-endpoints 0 tail endpoints {})
            (parse-requests []))))
