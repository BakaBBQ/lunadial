(ns lunadial.factory
  (:require [play-clj.g2d :refer :all]
            [lunadial.frame-data-loader :as loader]
            [lunadial.asset-utils :refer :all]
            [lunadial.logic :refer :all]
            [play-clj.utils :refer :all]
            [play-clj.core :refer :all])
  (:import [com.badlogic.gdx.math Vector2]))

(defn create-empty-actor
  [actor-name]
  {:x 0.0 :y 0.0 :vx 0.0 :vy 0.0 :ax 0.0 :ay 0.0 :texture-names [], :texture-id 0, :inst [], :hp 10000, :name actor-name :pointer 0 :stack [] :input-buffer []})

(defn assemble-actor-textures
  [actor]
  (assoc actor :texture-names (loader/load-texture-vectors (:name actor))))

(defn assemble-actor-insts
  [actor]
  (assoc actor :inst (loader/load-inst-vectors (:name actor))))

(defn create-dummy-actor
  "create actor without textures preloaded"
  [actor-name]
  (-> (create-empty-actor actor-name) ;; now pass actor to the assembly line
      assemble-actor-textures
      assemble-actor-insts))

(defn create-actor
  [actor-name]
  (-> (create-dummy-actor actor-name)
      preload-actor-textures))
