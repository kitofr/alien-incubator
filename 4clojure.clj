; (apply str (loop [del "," coll ["foo" "bar"] res []]
;   (if (empty? coll)
;     res
;     (recur del (rest coll) (conj res (cons (first coll) del))))))

; ((fn [& sq] 
;    (let [c (count sq)
;          ts (count (remove #(= % false) sq))]
;    (if 
;      (and 
;        (= 1 c)) 
;        (first sq)) 
;      false 
;      (and 
;        (< 0 ts)
;        (not (= c ts)))))
;     true true true)

; (drop 2 '(1 2 3 4 5))
; (take 2 [1 2 3 4 5])
; (#(loop [sq %1 n %2 v ()]
;     (if (empty? sq)
;       (vec (flatten v))
;       (recur (drop n sq) n (cons v (take (- n 1) sq)))))
;      [1 2 3 4 5 6 7 8] 3) 
; 
; ((fn drop-nth[n coll]
;   (vec
;     (when-let [s (seq coll)]
;      (flatten (cons (take (- n 1) s) (drop-nth n (drop n s))))))) 3 [1 2 3 4 5 6 7 8]) 
; 
;[1 2 4 5 7 8]

(ns clojure-problems
  (:use clojure.test))

; (defn problem49 
;   [n coll]
;   (cons (take n coll) (list (drop n coll))))
; 
;(deftest problem49-test
; (is (= (problem49 3 [1 2 3 4 5 6]) [[1 2 3] [4 5 6]]))
; (is (= (problem49 1 [:a :b :c :d]) [[:a] [:b :c :d]]))
; (is (= (problem49 2 [[1 2] [3 4] [5 6]]) [[[1 2] [3 4]] [[5 6]]])))

; (def problem51 [1 2 3 4 5])
; 
; (deftest problem51-test
;          (is (= [1 2 [3 4 5] [1 2 3 4 5]] (let [[a b & c :as d] problem51] [a b c d]))))

(defn problem61 
  [a b]
  (apply array-map
     (flatten
       (let [x (first a)
             y (first b)]
         (if (and x y)
           (cons (list x y) (problem61 (rest a) (rest b))))))))

(deftest problem61-tests
         (is (= (problem61 [:a :b :c] [1 2 3]) {:a 1, :b 2, :c 3}))
         (is (= (problem61 [1 2 3 4] ["one" "two" "three"]) {1 "one", 2 "two", 3 "three"}))
         (is (= (problem61 [:foo :bar] ["foo" "bar" "baz"]) {:foo "foo", :bar "bar"})))

(run-tests)
