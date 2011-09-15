(def width 50)
(def height 50)

(defn to-pos [x y]
  (+ (* y height) (mod x width)))

(defn populated? [pos]
  true)

(defn unpopulated? [pos]
  false)

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

(defn draw-world []
  (doseq [y (range height)]
    (print "|")
    (doseq [x (range width)]
      (print (if (populated? (to-pos x y)) 
               "*"
               " "))
      (println "|"))))
                

