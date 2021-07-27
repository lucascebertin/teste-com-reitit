(ns teste-com-reitit.components.webserver
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as server]
            [io.pedestal.interceptor.helpers :refer [before]]
            [reitit.pedestal :as pedestal]
            [teste-com-reitit.logging :as l]))

(defn- add-system [service]
  (before (fn [context] (assoc-in context [:request :components] service))))

(defn system-interceptors
  "Extend to service's interceptors to include one to inject the components
  into the request object"
  [service-map service]
  (update-in service-map
             [::server/interceptors]
             #(vec (->> % (cons (add-system service))))))

(defn base-service [port]
  {::server/port port
   ::server/type :jetty
   ::server/host "0.0.0.0"
   ::server/join? false
   ::server/routes []
   ::server/secure-headers {:content-security-policy-settings
                            {:default-src "'self'"
                             :style-src "'self' 'unsafe-inline'"
                             :script-src "'self' 'unsafe-inline'"
                             :img-src "'self' 'unsafe-inline' data: https://validator.swagger.io"}}})

(defn dev-init [service-map router]
  (-> service-map
      (merge {:env                     :dev
              ::server/join?           false
              ::server/secure-headers  {:content-security-policy-settings {:object-src "none"}}})
      (server/default-interceptors)
      (pedestal/replace-last-interceptor router)
      (server/dev-interceptors)))

(defn prod-init [service-map router]
  (-> service-map
      (merge {:env :prod})
      (server/default-interceptors)
      (pedestal/replace-last-interceptor router)))

(defn dev-or-production? [env]
  (if (= env :dev) dev-init prod-init))

(defn configure-server [port webserver router init-fn]
  (-> (base-service port)
      (init-fn (:router router))
      (system-interceptors webserver)
      (server/create-server)
      (server/start)))

(defrecord WebServer [config router]
  component/Lifecycle

  (start [this]
    (let [port (get-in config [:config :webserver :port])
          env (:env config)
          init-fn (dev-or-production? env)]
      (l/log :info :webserver :start {:env env :port port} ";; Initializing webserver")
      (assoc this :webserver (configure-server port this router init-fn))))

  (stop [this]
    (l/log :info :webserver :stop ";; Releasing webserver")
    (dissoc this :webserver)
    this))

(defn new-webserver []
  (map->WebServer {}))

