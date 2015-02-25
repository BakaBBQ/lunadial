(ns lunadial.input_buffer)


;; the buffer will look like
;;
;; [ latest -> oldest ]
(defn new-input-buffer
  []
  [])

(defn insert-key-to-buffer
  "returns a new buffer, limited in size with the new key-code inserted"
  [buffer key]
  (take 14 (cons key buffer)))

;; damn it... this is far from elegant
(defn result-guard
  [result]
  (case result
    [] [0]
    result))

(defn datalize-buffer
  [buffer]
  (->> buffer
       (remove #{0})
       (map (fn [v]
              (case v
                :a 0xa
                :b 0xb
                :c 0xc
                :d 0xd
                v )))
       result-guard))



(defn decide-result
  [buffer]
  (reduce #(+ (* %1 16) %2) (datalize-buffer buffer)))

(defn result2hex
  [num]
  (format "%x" num))
