(ns teste-com-reitit.database
  (:require [schema.core :as s]
            [next.jdbc.sql :as sql]
            [teste-com-reitit.schemas :as schemas]))

(def ^:private query-all-todos "SELECT * FROM todo_list")

(defn- sql-todo->todo-entity
  [{:todo_list/keys [id title description is_complete]}]
  {:id id 
   :title title 
   :description description 
   :is_complete is_complete})

(defn- sql-todos->todo-entities [sql-todos]
  (map sql-todo->todo-entity sql-todos))

(s/defn insert-todo! :- schemas/TodoEntity
  [{{dataset :dataset} :database} 
   todo :- schemas/TodoIn]
  (let [new-register (sql/insert! (dataset) :todo_list todo)]
    (sql-todo->todo-entity new-register)))

(s/defn get-todo-by-id! :- schemas/TodoEntity
  [{{dataset :dataset} :database} 
   id :- s/Int]
  (let [register (sql/get-by-id (dataset) :todo_list id)]
    (sql-todo->todo-entity register)))

(s/defn get-todos! :- [schemas/TodoEntity]
  [{{dataset :dataset} :database}]
  (let [registers (sql/query (dataset) [query-all-todos])]
    (sql-todos->todo-entities registers)))

(s/defn update-todo! :- schemas/TodoEntity
  [{{dataset :dataset} :database} 
   id :- s/Int todo :- schemas/TodoIn]
  (sql/update! (dataset) :todo_list todo {:id id})
  (merge todo {:id id}))

(s/defn delete-todo!
  [{{dataset :dataset} :database} 
   id :- s/Int]
  (sql/delete! (dataset) :todo_list {:id id}))
  
