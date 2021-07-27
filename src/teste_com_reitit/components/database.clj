(ns teste-com-reitit.components.database
  (:require [com.stuartsierra.component :as component]
            [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection]
            [teste-com-reitit.logging :as l])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defrecord Database [config]
  component/Lifecycle

  (start [this]
    (l/log ";; Initializing database component")
    (let [db-spec (get-in config [:config :database])
          ds (component/start (connection/component HikariDataSource db-spec))]
      (assoc this :database {:dataset ds})))

  (stop [this]
    (l/log ";; Releasing database component")
    (component/stop (get-in this [:database :dataset]))
    (assoc this :database {:dataset nil})))

(defn new-database [] (map->Database []))

