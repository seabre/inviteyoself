(defproject
  inviteyoself
  "0.1.0-SNAPSHOT"
  :description
  "FIXME: write description"
  :ring
  {:handler inviteyoself.handler/app,
   :init inviteyoself.handler/init,
   :destroy inviteyoself.handler/destroy}
  :ragtime
  {:migrations ragtime.sql.files/migrations,
   :database
   "jdbc:postgresql://localhost/inviteyoself?user=postgres&password=password"}
  :plugins
  [[lein-ring "0.8.10"]
   [lein-environ "0.5.0"]
   [lein-ancient "0.5.5"]
   [ragtime/ragtime.lein "0.3.6"]]
  :url
  "http://example.com/FIXME"
  :profiles
  {:uberjar {:aot :all},
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}},
   :dev
   {:dependencies
    [[ring-mock "0.1.5"]
     [ring/ring-devel "1.3.1"]
     [pjstadig/humane-test-output "0.6.0"]],
    :injections
    [(require 'pjstadig.humane-test-output)
     (pjstadig.humane-test-output/activate!)],
    :env {:dev true :db-user-name "postgres" :db-user-password "password" :db-host "localhost" :db-name "inviteyoself"}}}
  :main
  inviteyoself.core
  :jvm-opts
  ["-server"]
  :dependencies
  [[lib-noir "0.8.9"]
   [log4j
    "1.2.17"
    :exclusions
    [javax.mail/mail
     javax.jms/jms
     com.sun.jdmk/jmxtools
     com.sun.jmx/jmxri]]
   [http-kit "2.1.18"]
   [prone "0.6.0"]
   [noir-exception "0.2.2"]
   [com.taoensso/timbre "3.3.1"]
   [com.taoensso/tower "3.0.1"]
   [korma "0.4.0"]
   [selmer "0.7.1"]
   [org.clojure/clojure "1.6.0"]
   [environ "1.0.0"]
   [ring-server "0.3.1"]
   [postgresql/postgresql "9.1-901-1.jdbc4"]
   [ragtime "0.3.6"]
   [red-tape "1.0.0"]
   [commons-validator "1.4.0"]
   [slingshot "0.10.3"]]
  :repl-options
  {:init-ns inviteyoself.repl}
  :min-lein-version "2.0.0"
  :aliases {"start-production-server" ["with-profile" "production" "trampoline" "ring" "server"]
            "send-invites" ["trampoline" "run" "-m" "inviteyoself.tasks.send-invites"]
  })
