(ns teste-com-reitit.schemas
  (:require [schema.core :as s]))

(s/defschema TodoQuery
  {:id s/Int})

(s/defschema TodoOut
  {:id s/Int
   :title s/Str
   :description s/Str
   :is_complete s/Bool})

(s/defschema TodoIn
  {:title s/Str
   :description s/Str
   :is_complete s/Bool})

(s/defschema WorldTimeOut     
  {:abbreviation s/Str
   :client_ip s/Str
   :datetime s/Str
   :day_of_week s/Int
   :day_of_year s/Int
   :dst s/Bool
   :dst_from (s/maybe s/Str)
   :dst_offset s/Int
   :dst_until (s/maybe s/Str)
   :raw_offset s/Int
   :timezone s/Str
   :unixtime s/Int
   :utc_datetime s/Str
   :utc_offset s/Str
   :week_number s/Int})

(s/defschema TodoEntity 
  {:id s/Int
   :title s/Str
   :description s/Str
   :is_complete s/Bool})
