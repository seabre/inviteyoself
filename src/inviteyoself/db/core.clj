(ns inviteyoself.db.core
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [inviteyoself.db.schema :as schema]))

(defdb db schema/db-spec)

(defentity invites)

(defn create-invite [invite]
  (insert invites
    (values invite)))

(defn unique? [email]
  (= (:cnt
    (first
      (select invites
        (where (= :email email))
        (aggregate (count :*) :cnt)))) 0))

(defn all-not-invited [lmt]
  (select invites
    (where (= :invited false))
    (limit lmt)))

(defn delete-all-invited []
  (delete invites
    (where (= :invited true))))

(defn set-invited [ids]
  (update invites
    (set-fields {:invited true})
    (where {:id [in ids]})))

