(ns inviteyoself.db.schema
  (:require [environ.core :refer [env]])
)

(def db-spec
  {:subprotocol "postgresql"
   :subname (str "//" (env :db-host) "/" (env :db-name))
   :user (env :db-user-name)
   :password (env :db-user-password)})


