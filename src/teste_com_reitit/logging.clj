(ns teste-com-reitit.logging
  (:require [taoensso.timbre :as timbre]))

(defn log 
  ([args] (log :info args))
  ([level & args] (timbre/log level args)))

