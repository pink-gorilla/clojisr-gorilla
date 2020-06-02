(defproject org.pinkgorilla/clojisr-gorilla "0.0.6-SNAPSHOT"
  :description "Clojure<->R interop"
  :license {:name "MIT"}
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/release_username
                                     :password :env/release_password
                                     :sign-releases false}]]
  :min-lein-version "2.9.1"
  :min-java-version "1.11"

  :release-tasks [["vcs" "assert-committed"]
                  ["bump-version" "release"]
                  ["vcs" "commit" "Release %s"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy"]
                  ["bump-version"]
                  ["vcs" "commit" "Begin %s"]
                  ["vcs" "push"]]

   :managed-dependencies [
                          [com.taoensso/encore "2.119.0"]
   ]
  
  :dependencies [[org.clojure/clojure "1.10.1"]
                 ; ClojisR                 
                 [scicloj/clojisr "1.0.0-BETA11"]
                 
                 ;; pinkgorilla-vizualisation
                 [org.pinkgorilla/gorilla-renderable "3.0.7"] ; to implement pink-gorilla renderer
                 [com.rpl/specter "1.1.3"] ; clojisr.util, svg width/height injection
                 [org.clojure/data.xml "0.0.8"] ; make sure old version from tagsoup is not used
                 [clj-tagsoup/clj-tagsoup "0.3.0" ; to parse xml from the svg
                  :exclusions [org.clojure/clojure ; very, very old clojure version. 
                               org.clojure/core.specs.alpha ; damn old
                               org.clojure/data.xml ; damn old - "0.0.3"
                               ]]]

  :jvm-opts ["-Dclojure.tools.logging.factory=clojure.tools.logging.impl/jul-factory"]
  :source-paths ["src"]
  :resource-paths ["resources"]
  ;:repl-options {:init-ns ta.model.single}
  
   :pinkgorilla {:runtime-config "./profiles/notebook/config.edn"}
  
  
  :profiles {:demo {; run the pink-gorilla notebook (standalone, or in repl)
                       ; important to keep this dependency in here only, as we do not want to
                       ; bundle the notebook (big bundle) into clojisr library 
                    :source-paths ["profiles/notebook/src"]
                    :main notebook.main ; ^:skip-aot 
                    :dependencies [[org.pinkgorilla/gorilla-notebook "0.4.18-SNAPSHOT"]]
                    :repl-options {:welcome (println "Profile: gorilla")
                                   :init-ns notebook.main  ;; Specify the ns to start the REPL in (overrides :main in this case only)
                                   :init (start) ;; This expression will run when first opening a REPL, in the namespace from :init-ns or :main if specified.
                                   }}
             :dev
             {:dependencies [[clj-kondo "2019.11.23"]]
              :plugins      [[lein-cljfmt "0.6.6"]
                             [lein-cloverage "1.1.2"]]
              :aliases      {"clj-kondo" ["run" "-m" "clj-kondo.main"]}
              :cloverage    {:codecov? true
                                  ;; In case we want to exclude stuff
                                  ;; :ns-exclude-regex [#".*util.instrument"]
                                  ;; :test-ns-regex [#"^((?!debug-integration-test).)*$$"]
                             }
                   ;; TODO : Make cljfmt really nice : https://devhub.io/repos/bbatsov-cljfmt
              :cljfmt       {:indents {as->                [[:inner 0]]
                                       with-debug-bindings [[:inner 0]]
                                       merge-meta          [[:inner 0]]
                                       try-if-let          [[:block 1]]}}}}

  :plugins [[lein-ancient "0.6.15"]
            [min-java-version "0.1.0"]
            [org.pinkgorilla/lein-pinkgorilla "0.0.13"]]

  :aliases {"bump-version"
            ["change" "version" "leiningen.release/bump-version"]

            "lint" ^{:doc "Runs code linter"}
            ["clj-kondo" "--lint" "src"]

            "demo" ^{:doc "Runs pink-gorilla notebook"}
            ["with-profile" "+demo" "run" "-m" "notebook.main"]})
