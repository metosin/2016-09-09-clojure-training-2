(ns effective.data
  (:require [clj-http.client :as client]))

(defn random-users [n]
  (->
    (client/get
      "https://randomuser.me/api"
      {:as :json, :query-params {:results 10, :seed "effective"}})
    :body
    :results))

(defn xml-user []
  (->
    (client/get
      "https://randomuser.me/api"
      {:query-params {:results 1, :format "xml", :seed "effective"}})
    :body))

(defn github-org-data [org]
  (->
    (client/get
      (format
        "https://api.github.com/orgs/%s/repos"
        org)
      {:as :json, :query-params {:per_page 1000}})
    :body))

(defn clojars-stars [group]
  (->
    (client/get
      (format
        "https://clojars.org/api/groups/%s"
        group)
      {:as :json})
    :body))

(comment
  (random-users 10)
  (github-org-data "metosin")
  (clojars-stars "metosin")
  (xml-user))
