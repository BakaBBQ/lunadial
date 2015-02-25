(ns lunadial.components
  (:import (com.badlogic.gdx.math Vector2)
           (java.util.Stack)))


(defrecord Position [x y])
(defrecord InputHandler [])
(defrecord Hp [hp])
(defrecord Frames [inst texture-id]) ;;vector, int
(defrecord InputBuffer [^Stack stk])
(defrecord TextureGroup [textures])
