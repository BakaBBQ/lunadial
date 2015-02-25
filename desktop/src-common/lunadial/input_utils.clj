(ns lunadial.input-utils
  (:require [play-clj.core :refer :all]
            [play-clj.utils :refer :all])
  (:import [com.badlogic.gdx Gdx InputProcessor]))

(defn is-key-pressed
  "basically an alias for Gdx.input.isKeyPressed, does not use key-code internally, call key-code when using this function"
  [kc]
  (.isKeyPressed Gdx/input kc)
  )

(defn is-any-key-pressed
  []
  (is-key-pressed (key-code :any-key)))

(defn key-is-triggered
  [kc])

(defn save-all-key-code-states
  [screen])
