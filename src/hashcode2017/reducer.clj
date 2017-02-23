(ns hashcode2017.reducer
  (require [clojure.string :as str]
           [clojure.java.io :refer [reader writer]]))

(defn- try-cache [cache-id video-size cache-size]
  (if (< video-size cache-size) cache-id nil))

(defn- insert-video [arr position substract]
  (assoc arr position (- (nth arr position) substract)))

(defn- update-current-state [current-state video-id current-video-size target-cache]
  (-> current-state
      (update :results conj video-id)
      (update :caches insert-video target-cache current-video-size)))

(defn process-endpoints [current-state [video-id endpoint-id num-requests]
  (let [current-endpoint-caches (get-in current-state :endpoints (keyword (str endpoint-id)))
        current-video-size (nth (get current-state :videos) video-id)
        cache-remaining-sizes (get-in current-state :caches)
        target-cache (some #(try-cache % current-video-size (nth cache-remaining-sizes %)) current-endpoint-caches)]
    
    (if (not (nil? target-cache))
      (update-current-state current-state
                            video-id
                            current-video-size
                            target-cache)
      current-state)))