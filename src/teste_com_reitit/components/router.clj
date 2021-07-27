(ns teste-com-reitit.components.router
  (:require [com.stuartsierra.component :as component]
            [muuntaja.core :as m]
            [reitit.coercion.schema :as reitit.schema]
            [reitit.dev.pretty :as pretty]
            [reitit.http :as http]
            [reitit.http.coercion :as coercion]
            [reitit.http.interceptors.exception :as exception]
            [reitit.http.interceptors.multipart :as multipart]
            [reitit.http.interceptors.muuntaja :as muuntaja]
            [reitit.http.interceptors.parameters :as parameters]
            [reitit.pedestal :as pedestal]
            [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [teste-com-reitit.logging :as l]))

(defn- coercion-error-handler [status]
  (fn [exception _request]
    (l/log :error exception :coercion-errors (:errors (ex-data exception)))
    {:status status
     :body (if (= 400 status)
             (str "Invalid path or request parameters, with the following errors: "
                  (:errors (ex-data exception)))
             "Error checking path or request parameters.")}))

(defn- exception-info-handler [exception _request]
  (l/log :error exception "Server exception:" :exception exception)
  {:status 500
   :body   "Internal error."})

(def router-settings
  {:exception pretty/exception
   :data {:coercion reitit.schema/coercion
          :muuntaja (m/create
                     (-> m/default-options
                         (assoc-in [:formats "application/json" :decoder-opts :bigdecimals] true)))
          :interceptors [swagger/swagger-feature
                         (parameters/parameters-interceptor)
                         (muuntaja/format-negotiate-interceptor)
                         (muuntaja/format-response-interceptor)
                         (exception/exception-interceptor
                          (merge
                           exception/default-handlers
                           {:reitit.coercion/request-coercion  (coercion-error-handler 400)
                            :reitit.coercion/response-coercion (coercion-error-handler 500)
                            clojure.lang.ExceptionInfo exception-info-handler}))
                         (muuntaja/format-request-interceptor)
                         (coercion/coerce-response-interceptor)
                         (coercion/coerce-request-interceptor)
                         (multipart/multipart-interceptor)]}})

(defn router [routes]
  (pedestal/routing-interceptor
   (http/router routes router-settings)
   (ring/routes
    (swagger-ui/create-swagger-ui-handler
     {:path "/"
      :config {:validatorUrl nil
               :operationsSorter "alpha"}})
    (ring/create-resource-handler)
    (ring/create-default-handler))))

(defrecord Router [router]
  component/Lifecycle
  (start [this] 
    (l/log ";; Initializing router and router specs")
    this)
  (stop  [this] 
    (l/log ";; Releasing router and router specs")
    this))

(defn new-router
  [routes]
  (map->Router {:router (router routes)}))
