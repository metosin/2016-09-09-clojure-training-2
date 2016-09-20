(ns effective.apple
  (:require [ring.swagger.swagger2 :as swagger2]
            [ring.swagger.ui :as swagger-ui]
            [ring.util.http-response :as response]
            [clout.core :as clout]
            [clojure.walk :as walk]
            [manifold.deferred :as d]
            [cheshire.core :as json]
            [aleph.http :as http]
            [schema.core :as s]))

;;
;; TODO:
;; * add schema coercion (schema.coerce or ring.swagger.coerce)
;; * externalize json / format / content-negotiation
;; * add tests
;; * add data layers (server, components, etc.) on top
;;

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

(defn ping-handler [request]
  (response/ok (json/generate-string {:pong true})))

(defn get-apple [request]
  (let [timeout (rand-int 1000)]
    (d/timeout!
      (d/deferred)
      timeout
      (response/ok
        (format
          "here's the apple %s after %s ms"
          (-> request :path-parameters :id)
          timeout)))))

(defn random-user-async [request]
  (d/chain
    (http/get "https://randomuser.me/api?results=1")
    :body
    response/ok
    #(response/content-type % "application/json")))

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
   :paths {"/api/ping" {:get {::handler ping-handler}}
           "/api/random-user-async" {:get {::handler random-user-async}}
           "/apples" {:get {:summary "Lists apples"
                            :description "Returns emptly is if none match"
                            :tags ["apple"]
                            :parameters {}
                            ::handler (fn [request] (response/ok "apples!"))
                            :responses {200 {:schema [Apple]
                                             :description "Here be apples"}}}
                      :post {:summary "New apple"
                             :description "Creates a new apple"
                             :tags ["apple"]
                             ::handler (constantly "new apple")
                             :parameters {:body NewApple}
                             :responses {200 {:schema [Apple]
                                              :description "The apple"}}}}
           "/apples/:id" {:get {:summary "Get a apple"
                                ::handler get-apple
                                :description "404 ifnot"
                                :tags ["apple"]
                                :parameters {:path {:id s/Int}}
                                :responses {200 {:schema [Apple]
                                                 :description "Here be apples"}
                                            404 {:schema ErrorResponse
                                                 :description "Not found"}}}}}})

(defn compile-routes [application]
  (for [[path data] (:paths application)
        [method endpoint] data
        :let [compiled-route (clout/route-compile path)
              handler (::handler endpoint)]
        :when handler]
    [compiled-route method handler endpoint]))

;;
;; the dispatcher
;;

(defn create-api-routes [application]
  (let [routes (compile-routes application)]
    (fn [request]
      (or
        (some
          (fn [_ [route method handler _]]
            (if-let [info (and
                            (= method (:request-method request))
                            (clout/route-matches route request))]
              (handler (assoc request :path-parameters info))))
          routes)
        (response/not-found)))))

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

(defn- as-ring-swagger-json
  "we remove all our extra (namespaced) keys to make the spec valid"
  [application]
  (walk/postwalk
    (fn [x]
      (if (map? x) (dissoc x ::handler) x))
    application))

;;
;; bootstrapping the application
;;

(defn create-app [application]
  (some-fn
    (create-swagger-json-handler (as-ring-swagger-json application))
    (create-swagger-ui-handler "/")
    (create-api-routes application)))

(def app (create-app application))

;;
;; the server
;;

(comment
  (def server (server/start-server #'app {:port 3000})))
