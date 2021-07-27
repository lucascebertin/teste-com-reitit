(ns teste-com-reitit.core
  (:require [com.stuartsierra.component :as component]
            [teste-com-reitit.components.database :as database]
            [teste-com-reitit.components.config :as config]
            [teste-com-reitit.components.webserver :as webserver]
            [teste-com-reitit.components.router :as router]
            [teste-com-reitit.routes :as routes]
            [teste-com-reitit.logging :as l])
  (:gen-class))

(def system-atom (atom nil))

(defn- build-system-map []
  (component/system-map
   :config (config/new-config)
   :router (router/new-router routes/routes)
   :database (component/using (database/new-database) [:config])
   :webserver (component/using (webserver/new-webserver) [:config :router :database])))

(defn start-system! [system-map]
  (l/log "System starting...")
  (->> system-map
       component/start
       (reset! system-atom)))

(defn stop-system! []
  (l/log "System stopping...")
  (swap! system-atom (fn [s] (when s (component/stop s)))))

(defn -main
  [& _args]
  (.addShutdownHook (Runtime/getRuntime) (Thread. ^Runnable stop-system!))
  (start-system! (build-system-map)))

