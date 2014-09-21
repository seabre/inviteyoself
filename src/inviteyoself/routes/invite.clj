(ns inviteyoself.routes.invite
  (:require [compojure.core :refer :all]
            [inviteyoself.db.core :as invite]
            [red-tape.core :refer [defform]]
            [red-tape.cleaners :as cleaners]
            [inviteyoself.layout :as layout])
  (:use [slingshot.slingshot :only [throw+]]
        [ring.util.response])
  (:import [org.apache.commons.validator.routines EmailValidator]))

(defn ensure-valid-email [email]
  (if (not (.isValid (EmailValidator/getInstance) email))
    (throw+ "Email is not valid!")
    email))

(defn ensure-unique-email [email]
  (if (not (invite/unique? email))
    (throw+ "You've already submitted an invite recently!")
    email))

(defform invite-form {}
  :email [clojure.string/trim
          cleaners/non-blank
          ensure-valid-email
          ensure-unique-email])

(defn handle-get
  ([req]
    (handle-get req (invite-form)))
  ([req form]
    (layout/render
      "invite.html" form)))

(defn handle-post [req]
  (let [data (:params req)
        form (invite-form data)]
    (if (:valid form)
      (let [{:keys [email]} (:results form)]
        (invite/create-invite {:email email})
        (handle-get req form))
      (handle-get req form))))


(defroutes invite-routes
  (GET "/" [] handle-get)
  (POST "/" [] handle-post))
