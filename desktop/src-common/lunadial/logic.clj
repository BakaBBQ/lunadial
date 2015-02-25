(ns lunadial.logic
  (:require [lunadial.input_buffer :refer :all]))

;; the stack:
;; top [0x32 0x00 0x00 0x00] bottom



(defmulti execute-byte (fn [stack inst point char] (nth inst point)))

(defmethod execute-byte 0x00 [stack inst point char] ;; nop
  [stack inst (inc point) char])

(defmethod execute-byte 0x01 [stack inst point char] ;; push
  (let [the-bc-after (nth inst (inc point))]
    [(cons the-bc-after stack) inst (inc (inc point)) char]))

(defmethod execute-byte 0x02 [stack inst point char] ;; del
  [(rest stack) inst (inc point) char])

(defmethod execute-byte 0x03 [stack inst point char] ;; jmp
   (let [the-bc-after (nth inst (inc point))]
     [stack inst the-bc-after char]))

(defmethod execute-byte 0x04 [stack inst point char] ;; cond
  (let [f (first stack)
        s (second stack)
        the-bc-after (nth inst (inc point))]
    (if (== f s)
      [(nthrest stack 2) inst the-bc-after char]
      [(nthrest stack 2) inst (inc (inc point)) char])))

(defmethod execute-byte 0x05 [stack inst point char] ;; jas
  (let [f (first stack)]
    [(nthrest stack 1) inst f char]))

(defmethod execute-byte 0x06 [stack inst point char] ;; gnp
  (let [p (inc point)]
    [(cons p stack) inst (inc point) char]))

(defmethod execute-byte 0x07 [stack inst point char] ;; gcp
  (let [p point]
    [(cons p stack) inst (inc point) char]))

(defmethod execute-byte 0x10 [stack inst point char] ;; add
  (let [f (first stack)
        s (second stack)]
   [(cons (+ f s) (nthrest stack 2)) inst (inc point) char]))

(defmethod execute-byte 0x11 [stack inst point char] ;; sub
  (let [f (first stack)
        s (second stack)]
   [(cons (- f s) (nthrest stack 2)) inst (inc point) char]))

(defmethod execute-byte 0x12 [stack inst point char] ;; multi
  (let [f (first stack)
        s (second stack)]
   [(cons (* f s) (nthrest stack 2)) inst (inc point) char]))

(defmethod execute-byte 0x13 [stack inst point char] ;; div
  (let [f (first stack)
        s (second stack)]
    [(cons (/ f s) (nthrest stack 2)) inst (inc point) char]))

(defmethod execute-byte 0x30 [stack inst point char] ;; tex
  (let [f (first stack)]
    [(nthrest stack 1) inst (inc point) (assoc char :texture-id f)]))

(defmethod execute-byte 0x48 [stack inst point char]  ;; svx
  (let [f (first stack)]
    [(nthrest stack 1) inst (inc point) (assoc char :vx f)]))

(defmethod execute-byte 0x49 [stack inst point char]  ;; svy
  (let [f (first stack)]
    [(nthrest stack 1) inst (inc point) (assoc char :vy f)]))

(defmethod execute-byte 0x60 [stack inst point char]  ;; gip
  (let [input-result (decide-result (:input-buffer char))]
    [(cons input-result stack) inst (inc point) char]))

(defmethod execute-byte 0xf0 [stack inst point char] ;; bpt
  [stack inst (inc point) (assoc char :vx 10)])

(defmethod execute-byte 0xff [stack inst point char] ;; end frame
  [stack inst (inc point) char])


(defmethod execute-byte :default [stack inst point char] ;; just a testing workaround
  [(cons (nth inst point) stack) inst (inc point) char])

(defn execute-single-stack-step ;; the iterator wrapper for the multimethod
  [[stack inst point char]]
  (execute-byte stack inst point char))


(defn execute-bytes
  [inst stack step char]
  (last (take step (iterate execute-single-stack-step [stack inst 0 char]))))

(defn get-stack-steps
  [n]
  (take n (iterate execute-single-stack-step [])))

(defn execute-bytes-around-actor
  [a]
  (let [stack (:stack a)
        inst (:inst a)
        pointer (:pointer a)
        stack-result (execute-single-stack-step [stack inst pointer a])]
    (-> (nth stack-result 3)
        (assoc :stack (nth stack-result 0))
        (assoc :inst (nth stack-result 1))
        (assoc :pointer (nth stack-result 2)))))

(defn execute-number-of-bytes-around-actor
  [a step]
  (take step (iterate execute-bytes-around-actor a)))

;; http://stackoverflow.com/questions/18660687/clojure-take-while-and-n-more-items
(defn take-while-and-n-more [pred n coll]
    (let [[head tail] (split-with pred coll)]
         (concat head (take n tail))))

;; the iterate hell
(defn execute-until-stop
  [a]
  (let [acced-actor (execute-bytes-around-actor a)]
    (last (take-while-and-n-more
           (comp not
                 (fn [new-a]
                   (= (nth (:inst new-a) (:pointer new-a)) 0xff)))
           1
           (iterate execute-bytes-around-actor acced-actor)))))
