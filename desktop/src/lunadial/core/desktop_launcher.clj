(ns lunadial.core.desktop-launcher
  (:require [lunadial.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. lunadial "lunadial" 1280 720)
  (Keyboard/enableRepeatEvents true))
