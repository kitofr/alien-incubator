(ns clojure-problems
  (:use clojure.test))

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

; (defn problem61 
;   [a b]
;   (apply array-map
;      (flatten
;        (let [x (first a)
;              y (first b)]
;          (if (and x y)
;            (cons (list x y) (problem61 (rest a) (rest b))))))))
; 
; (deftest problem61-tests
;          (is (= (problem61 [:a :b :c] [1 2 3]) {:a 1, :b 2, :c 3}))
;          (is (= (problem61 [1 2 3 4] ["one" "two" "three"]) {1 "one", 2 "two", 3 "three"}))
;          (is (= (problem61 [:foo :bar] ["foo" "bar" "baz"]) {:foo "foo", :bar "bar"})))


;66
; Given two integers, write a function which returns the greatest common divisor.
; (defn problem66 [x y]
;   (cond
;     (> x y) (recur (- x y) y)
;     (< x y) (recur x (- y x))
;     :else x))
; 
; (deftest problem66-test
;          (is (= (problem66 2 4) 2))
;          (is (= (problem66 10 5) 5))
;          (is (= (problem66 5 7) 1))
;          (is (= (problem66 1023 858) 33)))


;62
; Given a side-effect free function f and an initial value x write a function which returns an infinite lazy sequence of 
; x, (f x), (f (f x)), (f (f (f x))), etc.
	
(defn problem62
  [f x]
  (lazy-seq (seq '( x (problem62 f (f x))))))

(take 5 (problem62 #(* 2 %) 1))

(deftest problem62
         (is (= (take 5 (__ #(* 2 %) 1)) [1 2 4 8 16]))
         (is (= (take 100 (__ inc 0)) (take 100 (range))))
         (is (= (take 9 (__ #(inc (mod % 3)) 1)) (take 9 (cycle [1 2 3])))))

(run-tests)

;81
; Write a function which returns the intersection of two sets. The intersection is the sub-set of items that each set has in common.
; 	
; (= (__ #{0 1 2 3} #{2 3 4 5}) #{2 3})
; (= (__ #{0 1 2} #{3 4 5}) #{})
; (= (__ #{:a :b :c :d} #{:c :e :a :f :d}) #{:a :c :d})

;90
; Write a function which calculates the Cartesian product of two sets.
; 	
; (= (__ #{"ace" "king" "queen"} #{"♠" "♥" "♦" "♣"})
;    #{["ace"   "♠"] ["ace"   "♥"] ["ace"   "♦"] ["ace"   "♣"]
;      ["king"  "♠"] ["king"  "♥"] ["king"  "♦"] ["king"  "♣"]
;      ["queen" "♠"] ["queen" "♥"] ["queen" "♦"] ["queen" "♣"]})
; (= (__ #{1 2 3} #{4 5})
;    #{[1 4] [2 4] [3 4] [1 5] [2 5] [3 5]})
; (= 300 (count (__ (into #{} (range 10))
;                  (into #{} (range 30)))))

;99
; Write a function which multiplies two numbers and returns the result as a sequence of its digits.
; 	
; (= (__ 1 1) [1])
; (= (__ 99 9) [8 9 1])
; (= (__ 999 99) [9 8 9 0 1])

;107
; Lexical scope and first-class functions are two of the most basic building blocks of a functional language like Clojure. When you combine the two together, you get something very powerful called lexical closures. With these, you can exercise a great deal of control over the lifetime of your local bindings, saving their values for use later, long after the code you're running now has finished.
; It can be hard to follow in the abstract, so let's build a simple closure. Given a positive integer n, return a function (f x) which computes xn. Observe that the effect of this is to preserve the value of n for use outside the scope in which it is defined.
; 
; 	
; (= 256 ((__ 2) 16),
;        ((__ 8) 2))
; (= [1 8 27 64] (map (__ 3) [1 2 3 4]))
; (= [1 2 4 8 16] (map #((__ %) 2) [0 1 2 3 4]))

; (run-tests)
