(ns effective.specter
  (:require [com.rpl.specter :refer :all]))

;; https://github.com/nathanmarz/specter
;; samples from the README (c) nathanmarz

(transform
  [ALL :a even?]
  inc
  [{:a 1} {:a 2} {:a 4} {:a 3}])

(transform
  [MAP-VALS MAP-VALS]
  inc
  {:a {:aa 1} :b {:ba -1 :bb 2}})

(select
  [ALL ALL #(= 0 (mod % 3))]
  [[1 2 3 4] [] [5 3 2 18] [2 4 6] [12]])
