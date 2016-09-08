(defproject metosin/effective-clojure "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0-alpha10"]

                 ;; cool stuff
                 [com.rpl/specter "0.13.0"]

                 ;; swagger stuff
                 [metosin/ring-swagger "0.22.10"]
                 [metosin/ring-swagger-ui "2.2.2-0"]
                 [metosin/schema-tools "0.9.0"]

                 ;; schemas
                 [prismatic/schema "1.1.3"]
                 [prismatic/plumbing "0.5.3"]

                 ;; http-stuff
                 [clj-http "2.2.0"]
                 [aleph "0.4.1"]
                 [clout "2.1.2"]]
  :profiles {:dev {:global-vars {*warn-on-reflection* false} ;; bring the noice
                   :dependencies [[aprint "0.1.3"]
                                  [criterium "0.4.4"]]
                   :plugins [[lein-nodisassemble "0.1.3"]]}})
