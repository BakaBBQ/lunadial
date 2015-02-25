(defproject lunadial "0.0.1-SNAPSHOT"
  :description "FIXME: write description"

  :dependencies [[com.badlogicgames.gdx/gdx "1.5.0"]
                 [com.badlogicgames.gdx/gdx-backend-lwjgl "1.5.0"]
                 [com.badlogicgames.gdx/gdx-box2d "1.5.0"]
                 [com.badlogicgames.gdx/gdx-box2d-platform "1.5.0"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-bullet "1.5.0"]
                 [com.badlogicgames.gdx/gdx-bullet-platform "1.5.0"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-platform "1.5.0"
                  :classifier "natives-desktop"]
                 [org.clojure/clojure "1.6.0"]
                 [cheshire "5.4.0"]
                 [brute "0.2.0"]
                 [play-clj "0.4.3"]]

  :source-paths ["src" "src-common"]
  :resource-paths ["resources"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [lunadial.core.desktop-launcher]
  :plugins [[cider/cider-nrepl "0.8.2"]]
  :main lunadial.core.desktop-launcher)
