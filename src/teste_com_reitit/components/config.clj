(ns teste-com-reitit.components.config
  (:require [config.core :refer [env]]
            [com.stuartsierra.component :as component]
            [teste-com-reitit.logging :as l]))

(defrecord Config []
  component/Lifecycle

  (start [component]
    (l/log ";; Reading configuration entries")
    (let [db (:database env)
          webserver (:webserver env)]
      (assoc component :config {:database db :webserver webserver})))

  (stop [component]
    (l/log ";; Releasing configuration entries")
    (assoc component :config nil)))

(defn new-config [] (map->Config []))

