(ns lunadial.frame-data-loader
  (:require [cheshire.core :refer :all]
            [play-clj.core :refer :all]
            [play-clj.utils :refer :all]))




(defn load-internal-json
  [filename]
  (let [s (slurp filename)]
    (parse-string s)))

(defn load-frame-datas
  []
  (load-internal-json "mamizou/frames.json"))

(defn load-texture-vectors
  [actor-name]
  (load-internal-json (str "resources/" actor-name "/textures.vec.json")))


(defn load-inst-vectors
  [actor-name]
  (load-internal-json (str "resources/" actor-name "/inst.json")))
