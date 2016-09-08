(ns effective.transformations
  (:require [effective.data :as data]
            [clojure.walk :as walk]
            [clojure.zip :as zip]
            [schema.core :as s]))

(defonce users (data/random-users 10))

;; get second users's password

;; count all females

;; collect all username + passwords

;; transform all keys to strings

;; remove all :password fields

;; transform all entrys to conform the schema

(s/defschema User
  {:email s/Str
   :name {:first s/Str
          :last s/Str}
   :gender (s/enum :male :female)})

;; convert xml-data to json-like clojure-map

(defonce xml-user (data/xml-user))
