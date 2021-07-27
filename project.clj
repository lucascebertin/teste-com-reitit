(defproject teste-com-reitit "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [yogthos/config "1.1.8"]
                 [com.github.seancorfield/honeysql "2.0.0-rc5"]
                 [com.stuartsierra/component "1.0.0"]
                 [com.zaxxer/HikariCP "5.0.0"]
                 [org.postgresql/postgresql "42.2.23"]
                 [seancorfield/next.jdbc "1.2.659"]
                 [prismatic/schema "1.1.12"]
                 [io.pedestal/pedestal.jetty  "0.5.9"]
                 [io.pedestal/pedestal.service "0.5.9"]
                 [org.clojure/data.json "2.4.0"]
                 [metosin/reitit "0.5.13"]
                 [metosin/reitit-pedestal "0.5.13"]
                 [metosin/reitit-swagger "0.5.13"]
                 [metosin/reitit-swagger-ui "0.5.13"]
                 [com.taoensso/timbre "5.1.2"]
                 [clj-http "3.12.3"]
                 [cheshire "5.10.1"]
                 [migratus "1.3.5"]]
  :plugins [[migratus-lein "0.7.3"]]
  :main ^:skip-aot teste-com-reitit.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :migratus {:store :database
             :migration-dir "migrations"
             :db {:classname "org.postgresql.Driver"
                  :subprotocol "postgresql"
                  :subname ~(get (System/getenv) "DB_SUBNAME" "//localhost/todo")
                  :user ~(get (System/getenv) "DB_USER" "user")
                  :password ~(get (System/getenv) "DB_PASSWORD" "root")
                  :port ~(get (System/getenv) "DB_PORT" 5432)}})
