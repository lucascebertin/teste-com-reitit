(ns teste-com-reitit.routes
  (:require [reitit.swagger :as swagger]
            [teste-com-reitit.schemas :as schemas]
            [teste-com-reitit.ports.todo.http-in :as todo-in]
            [teste-com-reitit.ports.time.http-in :as time-in]
            [schema.core :as s]))

(def ^:private swagger-config-route
  ["/swagger.json" {:get {:no-doc true
                          :swagger {:info {:title "todo list" :description "todo list example"}}
                          :handler (swagger/create-swagger-handler)}}])

(def ^:private time-routes
  ["/time" {:swagger {:tags ["time"]}}
   ["/" {:get {:summary "get time from Sao_Paulo"
                  :handler time-in/get-current-time
                  :responses {200 {:body schemas/WorldTimeOut}
                              500 {:body s/Str}}}}]])

(def ^:private todo-routes
  ["/todo" {:swagger {:tags ["todo"]}}

   ["/:id" {:get {:summary "get todo by id"
                  :handler todo-in/get-todo-by-id
                  :parameters {:query schemas/TodoQuery}
                  :responses {200 {:body schemas/TodoOut}
                              500 {:body s/Str}
                              404 {:body s/Str}}}

            :delete {:summary "delete todo by id"
                     :handler todo-in/delete-todo-by-id
                     :parameters {:query schemas/TodoQuery}
                     :responses {200 {}
                                 500 {:body s/Str}}}

            :put {:summary "update todo by id"
                  :handler todo-in/update-todo-by-id
                  :parameters {:body schemas/TodoIn
                               :query schemas/TodoQuery}
                  :responses {200 {:body schemas/TodoOut}
                              500 {:body s/Str}}}}]

   ["/"    {:get {:summary "get all todos"
                  :handler todo-in/get-todos
                  :responses {200 {:body [schemas/TodoOut]}
                              500 {:body s/Str}}}

            :post {:summary "get all todos"
                   :handler todo-in/add-todo
                   :parameters {:body schemas/TodoIn}
                   :responses {201 {:body schemas/TodoOut}
                               500 {:body s/Str}}}}]])

(def routes
  [swagger-config-route
   todo-routes
   time-routes])
