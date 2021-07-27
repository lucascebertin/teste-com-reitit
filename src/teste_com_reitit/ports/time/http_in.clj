(ns teste-com-reitit.ports.time.http-in
  (:require [clj-http.client :as client]
            [schema.core :as s]
            [teste-com-reitit.schemas :as schemas]))

(s/defn get-current-time :- schemas/WorldTimeOut
  [{_ :components}]
  (let [response (client/get "http://worldtimeapi.org/api/timezone/America/Sao_Paulo" {:as :json})]
    {:status 200 :body (:body response)}))

