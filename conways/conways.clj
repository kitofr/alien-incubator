(ns com.kitofr.conways)

(def width 50)
(def height 50)

(def life (ref {}))

(defn to-pos [x y]
  (+ (* y height) (mod x width)))

(defn unpopulated? [pos]
  (nil? (get @life pos)))

(defn populated? [pos]
  (not (unpopulated? pos)))

(defn neighbors [pos]
  [])

(defn neighbor-count [pos]
  (count (neighbors pos)))

(defn loney? [pos]
  (< (neighbor-count pos) 2))

(defn overpopulated? [pos]
  (>= (neighbor-count pos) 4))

(defn survivor? [pos]
  (or (= (neighbor-count pos) 2)
      (= (neighbor-count pos) 3)))

(defn fertile? [pos]
  (= (neighbor-count pos) 3))

(defn populate! [pos]
  (dosync (ref-set life (assoc @life pos true))))

;(defn draw-world []
;  (doseq [y (range height)]
;    (print "|")
;    (doseq [x (range width)]
;      (print (if (populated? (to-pos x y)) 
;               "*"
;               " "))
;      (println ">"))))
;                

