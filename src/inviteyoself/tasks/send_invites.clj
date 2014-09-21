(ns inviteyoself.tasks.send-invites
  (:require [inviteyoself.db.core :as invite]
            [environ.core :refer [env]])
  (:use [clojure.java.shell :only [sh]])
  (:gen-class))

(def invite-limit 10)
(def automator-path "resources/inviteyoself-automator/inviteyoself-automator.js")

(defn get-from-maps [kwd vec]
  (map #(kwd %) vec))

(defn automator-args [subdomain username password emails]
  ["casperjs"
    automator-path
    (str "--subdomain=" subdomain)
    (str "--username=" username)
    (str "--password=" password)
    (str "--emails=" emails)])

(defn run-inviteyoself-automator [subdomain username password emails]
  (:exit (apply sh (automator-args subdomain username password emails))))

(defn -main [& args]
  ; clean up folks already invited
  (invite/delete-all-invited)
  (if (> (count (invite/all-not-invited invite-limit)) 0)
    (let [not-invited (invite/all-not-invited invite-limit)
          status-code (run-inviteyoself-automator
                        (env :slack-subdomain)
                        (env :slack-username)
                        (env :slack-password)
                        (clojure.string/join "," (get-from-maps :email not-invited)))]
      (if (= status-code 0)
        (do
          (invite/set-invited (get-from-maps :id not-invited))
          (println "Success!")
          (System/exit 0))
        (do
          (println "Failed!")
          (System/exit 1))))
  (println "No invitations need to be sent!")))
