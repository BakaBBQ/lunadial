(ns lunadial.asset-utils
  (:require  [play-clj.core :refer :all]
             [play-clj.g2d :refer :all])
  (:import  [com.badlogic.gdx.graphics Texture]))


(defn retrieve-texture-from-path
  "returns a libgdx Texture object from asset-manager, requires full path"
  [texture-full-name]
  (.get play-clj.utils/*asset-manager* texture-full-name))

(defn assemble-actor-name-texture-path
  "returns a full INTERNAL path to the actor, a very fundamental utility"
  [actor-name path-last-part]
  (str "resources/" actor-name "/" path-last-part))

(defn preload-actor-textures
  "forces the asset-manager to load all the textures associated with an actor; returns the actor itself"
  [actor]
  (let [actor-name (:name actor)]
    (doseq [path-last-part (:texture-names actor)]
      (texture (assemble-actor-name-texture-path actor-name path-last-part))))
  actor)
