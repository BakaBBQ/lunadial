(ns lunadial.rendering
  (:require [brute.entity :as e])
  (:import [com.badlogic.gdx.graphics.g2d SpriteBatch]))

(defn start
  [system]
  (as-> {:sprite-batch (SpriteBatch.)}
        renderer
        (assoc system :renderer renderer)))

(defn- render-frames
  [system]
  (let [sprite-batch (:sprite-batch (:renderer system))]
    (.begin sprite-batch)
    (doseq [entity (e/get-all-entities-with-component system Frames TextureGroup Position)]
      (let [texture-group (e/get-component system entity TextureGroup)
            frames (e/get-component system entity Frames)
            pos (e/get-component system entity Position)
            texture (nth (:textures texture-group) 0)]
        (.draw sprite-batch texture (:x pos) (:y pos))))
    (.end sprite-batch)))

(defn process-one-game-tick
  [system _]
  (render-frames system)
  system)
