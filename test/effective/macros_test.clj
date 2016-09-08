(ns effective.macros-test
  (:require [clojure.test :refer :all]
            [plumbing.core :as p]
            [plumbing.fnk.pfnk :as pfnk]
            [schema.core :as s]))

;;
;; Create a ring-handler function, which takes
;; :request :query-params x & y (Longs)
;; :request :header-params token (Strings)
;; :user id (Long) & name (String)
;; * returns [(+ x y) token id name]
;; * we should be able to extract the schema out for api-docs & external coercion
;;

(declare input-schema)

(declare handle-request)

(p/defnk handle-request
  "This is a handler function consuming a map with :user and :request (ring)
  and producing an output"
  [[:request
    [:query-params x :- s/Int, y :- s/Int]
    [:header-params token :- s/Str]]
   [:user id :- s/Int, name :- s/Str]]
  [(+ x y) token id name])

(def request {:request {:query-params {:x 1, :y 2}
                        :header-params {:token "token"}}
              :user {:id 1, :name "Tommi"}})

(defn input-schema [f]
  (pfnk/input-schema f))

(deftest request-handler-test
  (is (= [3 "token" 1 "Tommi"]
         (handle-request request)))

  (is (= {:user {:name s/Str
                 :id s/Int
                 s/Keyword, s/Any},
          :request {:header-params {:token s/Str
                                    s/Keyword s/Any},
                    :query-params {:y s/Int
                                   :x s/Int
                                   s/Keyword s/Any},
                    s/Keyword s/Any},
          s/Keyword s/Any}
         (input-schema handle-request))))
