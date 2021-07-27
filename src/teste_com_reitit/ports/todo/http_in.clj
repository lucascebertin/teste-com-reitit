(ns teste-com-reitit.ports.todo.http-in
  (:require [teste-com-reitit.schemas :as schemas]
            [teste-com-reitit.database :as db]
            [schema.core :as s]))

(s/defn get-todos :- [schemas/TodoOut]
  [{{database :database} :components}]
  (let [todos (db/get-todos! database)]
    (when empty? todos
      {:status 404 :body "Todo not found"}
      {:status 200 :body todos})))

(s/defn add-todo :- schemas/TodoOut
  [{{database :database} :components 
    {body :body} :parameters}]
  (let [todo (db/insert-todo! database body)]
    {:status 201 :body todo}))

(s/defn get-todo-by-id :- schemas/TodoOut
  [{{database :database} :components 
    {{id :id} :query} :parameters}]
  (let [todo (db/get-todo-by-id! database id)]
    (when todo
      {:status 200 :body todo}
      {:status 404 :body "Todo not found"})))

(s/defn update-todo-by-id :- schemas/TodoOut
  [{{database :database} :components 
    {{id :id} :query body :body} :parameters}]
  (let [todo (db/update-todo! database id body)]
    {:status 200 :body todo}))

(s/defn delete-todo-by-id :- schemas/TodoOut
  [{{database :database} :components 
    {{id :id} :query} :parameters}]
  (db/delete-todo! database id)
  {:status 200})


