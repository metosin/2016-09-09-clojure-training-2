(ns effective.core-test
  (:require [criterium.core :as cc]))

;; dotimes
;; criterium

(def request {:headers {"Content-type" "application/json"}})

(defn content-type [request]
  (get-in request [:headers "Content-type"]))

(def pam (apply array-map (map identity (zipmap (range 1000) (range 1000)))))
(def phm (apply hash-map (map identity (zipmap (range 1000) (range 1000)))))

(def plus1-times2-minus1
  (comp
    (map inc)
    (map (partial * 2))
    (map dec)))

(comment
  (cc/quick-bench
    (into [] plus1-times2-minus1 (range 1000000)))

  (cc/quick-bench
    (->> (range 1000000)
         (map inc)
         (map (partial * 2))
         (map dec)
         (into [])))

  (cc/quick-bench
    (->> (range 1000000)
         (map (comp inc (partial * 2) dec))
         (into []))))
