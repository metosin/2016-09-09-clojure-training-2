(ns effective.apple
  (:require [ring.swagger.swagger2 :as swagger2]
            [ring.swagger.ui :as swagger-ui]
            [ring.util.http-response :as response]
            [cheshire.core :as json]
            [aleph.http.server :as server]
            [schema.core :as s]))

;;
;; Example application
;;

(s/defschema Apple
  {:id s/Int,
   :name s/Str
   (s/optional-key :origin) {:street s/Str
                             :city s/Str
                             :zip s/Int}})

(s/defschema NewApple
  (dissoc Apple :id))

(s/defschema ErrorResponse
  {:code s/Int
   :message s/Str})

(def application
  {:info {:version "1.0.0"
          :title "Apples"
          :description "Apple app"
          :termsOfService "http://www.metosin.fi/training/20160909/"
          :contact {:name "Metosin Training"
                    :email "training@metosin.fi"
                    :url "http://www.metosin.fi"}
          :license {:name "Eclipse Public License"
                    :url "http://www.eclipse.org/legal/epl-v10.html"}}
   :tags [{:name "apple"
           :description "Apple api"}]
   :paths {"/api/ping" {:get {}}
           "/apples" {:get {:summary "Lists apples"
                            :description "Returns emptly is if none match"
                            :tags ["apple"]
                            :parameters {}
                            :responses {200 {:schema [Apple]
                                             :description "Here be apples"}}}
                      :post {:summary "New apple"
                             :description "Creates a new apple"
                             :tags ["apple"]
                             :parameters {:body NewApple}
                             :responses {200 {:schema [Apple]
                                              :description "The apple"}}}}
           "/apples/:id" {:get {:summary "Get a apple"
                                :description "404 ifnot"
                                :tags ["apple"]
                                :parameters {:path {:id s/Int}}
                                :responses {200 {:schema [Apple]
                                                 :description "Here be apples"}
                                            404 {:schema ErrorResponse
                                                 :description "Not found"}}}}}})

;;
;; the dispatcher
;;

(defn create-api-routes [application]
  (fn [request]
    (response/not-found)))

;;
;; swagger.json & swagger-ui
;;

(defn create-swagger-ui-handler [path]
  (swagger-ui/swagger-ui path))

(defn create-swagger-json-handler [swagger-data]
  (fn [{:keys [uri]}]
    (if (= uri "/swagger.json")
      (response/ok
        (json/generate-string
          (swagger2/swagger-json swagger-data))))))

;;
;; bootstrapping the application
;;

(defn create-app [application]
  (create-api-routes application))

(def app (create-app application))

;;
;; the server
;;

(comment
  (def server (server/start-server #'app {:port 3000})))
