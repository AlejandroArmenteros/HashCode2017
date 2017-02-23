(ns hashcode2017.core
  (require [clojure.string :as str]
           [clojure.java.io :refer [reader writer]]
           [hashcode2017.reducer :refer [process-endpoints]])
  (:gen-class))

(def videos-atom (atom {}))
(def endpoints-atom (atom {}))
(def caches-atom (atom {}))
(def requests-atom (atom []))

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
        pret))


(defn- parse-endpoint [[[datacenter-latency caches-connected] & tail]]
  [datacenter-latency caches-connected
          (take caches-connected tail)
          (drop caches-connected tail)])

(defn- parse-endpoints [e t endpoints result]
  (if (< e endpoints)
  					(let [[datacenter-latency caches-connected caches-e t2] (parse-endpoint t)]
  						(recur (inc e) t2 endpoints (assoc result (keyword (str e)) (calculate-latencies caches-e))))
            (do (reset! endpoints-atom result)
                t)))

(defn- parse-requests [tail]
	(reset! requests-atom 
    (reverse 
      (sort-by #(nth % 2) 
                tail))))

(defn- config [caches]
  {:videos @videos-atom
   :endpoints @endpoints-atom
   :caches @caches-atom
   :results (reduce #(assoc %1 (keyword (str %2)) []) {} (range caches))})

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (if-let [filename (first args)]
  	(let [[[videos endpoints request-desc cache size] & [videos-size & tail]] (-> filename parse-file)]
  		      (reset! caches-atom (take cache (repeat size)))
            (reset! videos-atom videos-size)
            (-> (parse-endpoints 0 tail endpoints {})
                (parse-requests))
    (reduce #(process-endpoints (config cache) %) @requests-atom))))
