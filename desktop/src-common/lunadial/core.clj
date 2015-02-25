(ns lunadial.core
  (:require [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.utils :refer :all]
            [brute.entity :as e]
            [brute.system :as s]
            [lunadial.frame-data-loader :refer :all]
            [lunadial.logic :as l]
            [lunadial.asset-utils :refer :all]
            [lunadial.input-utils :refer :all]
            [lunadial.input_buffer :refer :all]
            [lunadial.factory :as f])
  (:import [com.badlogic.gdx.graphics.g2d SpriteBatch Batch ParticleEffect]
           [com.badlogic.gdx.graphics Texture]
           [com.badlogic.gdx Gdx]
           [com.badlogic.gdx.math Vector2]
           [com.badlogic.gdx.graphics FPSLogger]))


(defonce manager (asset-manager))

(set-asset-manager! manager)

(defn update-key-inserting-to-buffer
  [actor kc bc]
  (if (is-key-pressed kc)
    (assoc actor :input-buffer (insert-key-to-buffer (:input-buffer actor) bc))
    actor))

(defn update-empty-key-inserting-to-buffer
  [actor]
  (if (not (is-any-key-pressed))
    (assoc actor :input-buffer (insert-key-to-buffer (:input-buffer actor) 0))
    actor))

(defn update-input
  [entity]
  (-> entity
      (update-key-inserting-to-buffer (key-code :j) :a)
      (update-key-inserting-to-buffer (key-code :k) :b)
      (update-key-inserting-to-buffer (key-code :l) :c)

      (update-key-inserting-to-buffer (key-code :a) 4)
      (update-key-inserting-to-buffer (key-code :d) 6)
      (update-key-inserting-to-buffer (key-code :s) 2)
      (update-key-inserting-to-buffer (key-code :w) 8)
      update-empty-key-inserting-to-buffer))

(defn update-entity-velocity
  [entity]
  (-> entity
      (update-in [:vx] (fn [x-velocity] (+ x-velocity (:ax entity))))
      (update-in [:vy] (fn [y-velocity] (+ y-velocity (:ay entity))))))

(defn update-entity-pos
  [entity]
  (-> entity
      (update-in [:x] (fn [x-pos] (+ x-pos (:vx entity))))
      (update-in [:y] (fn [y-pos] (+ y-pos (:vy entity))))))

(defn update-debug
  [entity]
  (prn (:input-buffer entity))
  (prn (result2hex (decide-result (:input-buffer entity))))
  (comment println (is-any-key-pressed))
  (comment println (is-key-pressed (key-code :d)))
  (comment println (:pointer entity))
  entity)

(defn get-actor-textures
  "gets a vector of libgdx Textures from the actor using the actor's :texture-name vector"
  [actor]
  (map (fn [texture-name]
         (let [texture-full-name (assemble-actor-name-texture-path (:name actor) texture-name)]
           (retrieve-texture-from-path texture-full-name)))
       (:texture-names actor)))

;; TODO
;; ---------
;; | needs to get rid of fetching all the textures and then nth it
;; | might be too inefficient
;; ---------
(defn draw-actors-textures
  "the render loop function"
  [{:keys [renderer] :as screen} entities]
  (let [^SpriteBatch batch (.getBatch renderer)]
    (.begin batch)
    (doseq [entity entities]
      (let [^Texture drawing-texture (nth (get-actor-textures entity) (:texture-id entity))
            x (:x entity)
            y (:y entity)]
        (.draw batch drawing-texture (float x) (float y))))
    (.end batch))
  entities)

(defn update-actors
  [{:keys [renderer] :as screen} entities]
  (for [entity entities]
    (-> entity
        l/execute-until-stop
        update-input
        update-entity-velocity
        update-entity-pos
        update-debug)))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage))
    (let [reimu (f/create-actor "reimu")]
      [reimu]))

  :on-render
  (fn [screen entities]
    (clear!)
    (->> entities
           (update-actors screen)
           (draw-actors-textures screen))))

(defscreen text-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (assoc (label "0" (color :white))
           :id :fps
           :x 5))

  :on-render
  (fn [screen entities]
    (->> (for [entity entities]
           (case (:id entity)
             :fps (doto entity (label! :set-text (str (game :fps))))
             entity))
         (render! screen)))

  :on-resize
  (fn [screen entities]
    (height! screen 300)))

(defgame lunadial
  :on-create
  (fn [this]
    (set-screen! this main-screen text-screen)))
