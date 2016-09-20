(ns effective.core-test
  (:require [criterium.core :as cc]))

;; dotimes
;; criterium

(def request {:headers {"Content-type" "application/json"}})

(defn content-type [request]
  (get (:headers request) "Content-type"))

(defn content-type2 [request]
  (-> request :headers (get "Content-type")))

(macroexpand-1
  `(-> request :headers (get "Content-type")))

(time
  (dotimes [_ 1000000]
    (content-type request)))

(time
  (dotimes [_ 1000000]
    (content-type2 request)))

(def pam (apply array-map (mapcat identity (zipmap (range 1000) (range 1000)))))
(def phm (apply hash-map (mapcat identity (zipmap (range 1000) (range 1000)))))

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
