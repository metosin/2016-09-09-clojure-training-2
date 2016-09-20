(ns effective.transformations
  (:require [effective.data :as data]
            [clojure.walk :as walk]
            [clojure.zip :as zip]
            [schema.core :as s]))

(defonce users (data/random-users 10))

users
;; get second users's password

(get-in users [1 :login :password])

;; count all females

(count (filter (fn [user] (= (:gender user) "female")) users))

(->> users
     (filter #(= (:gender %) "female"))
     count)

;; collect all username + passwords

(->> users
     (map :login)
     (map (juxt :username :password)))

;; transform all keys to strings

(walk/prewalk
  (fn [x]
    (if (keyword? x) (name x) x))
  users)

;; remove all :password fields

;; transform all entrys to conform the schema

(s/defschema User
  {:email s/Str
   :name {:first s/Str
          :last s/Str}
   :gender (s/enum :male :female)})

;; convert xml-data to json-like clojure-map

(defonce xml-user (data/xml-user))
