(def width 100)
(def height 30)
(def jungle '(45 10 10 10))
(def plants (ref {}))
(def day (ref 0))

(defn gethash [obj]
  (if (= "1.1.0-master-SNAPSHOT" (clojure-version))
    (.GetHashCode obj)
    (.hashCode obj)))

(defn plant-energy []
  (rand-int 10))

(defn random-plant [left top width height]
  (let [pos (list (+ left (rand-int width)) (list (+ top (rand-int height))))]
    (dosync (alter plants assoc (gethash pos) true))))

(defn add-plants []
  (apply #'random-plant jungle)
  (random-plant 0 0 width height))

(defstruct animal :x :y :energy :dir :genes)

(def animals 
  (ref (list (struct-map animal 
                    :x (int (/ width 2))
                    :y (int (/ height 2))
                    :energy 1000
                    :dir 0
                    :genes (into [] (take 8
                                 (repeatedly #(rand-int 10))))))))

(defn move [animal]
  (let [dir (:dir animal)
        x (:x animal)
        y (:y animal)]
    (assoc animal
           :x (mod (+ x
                      (cond 
                        (and (>= dir 2) (< dir 5)) 1
                        (or (= dir 1) (= dir 5)) 0
                        :else -1)
                      width)
                   width)
           :y (mod (+ y
                      (cond (and (>= dir 0) (< dir 3)) -1
                            (and (>= dir 4) (< dir 7)) 1
                            :else 0)
                      height)
                   height)
           :energy (dec (animal :energy)))))

(defn turn [animal]
  (let [x (rand-int (apply #'+ (animal :genes)))]
    (letfn [(angle [genes x]
                   (let [xnu (- x (first genes))]
                     (if (< xnu 0)
                       0
                       (+ 1 (angle (rest genes) xnu)))))]
      (assoc animal :dir
             (mod (+ (animal :dir) (angle (animal :genes ) x)) 8)))))

(defn eat [animal]
  (let [pos (list (animal :x) (animal :y))]
    (when plants (gethash pos)
      (dosync (alter plants assoc (gethash pos) nil)
      (assoc animal :energy (+ (animal :energy) (plant-energy)))))))

(def reproduction-energy 200)

(defn mutate [animal]
  (let [gene-index (rand-int 8)
        new-genes (assoc (animal :genes) gene-index (max 1 (+ (nth (animal :genes) gene-index) (rand-int 3) -1)))]
    new-genes))

(defn reduce-energy [animal]
  (assoc animal :energy (int (/ (animal :energy) 2))))

(defn reproduce [animal]
  (let [e (animal :energy)]
    (if (>= e reproduction-energy)
      (let [child (reduce-energy animal) 
            new-genes (mutate animal)]
        (list 
          (reduce-energy animal) 
          (assoc child :genes new-genes)))
    (list animal))))

(defn kill-animals []
  (filter #(> (% :energy) 0) @animals))

(defn update-world []
  (dosync 
    (ref-set animals (kill-animals))
    (ref-set animals (map #(eat (move (turn %))) @animals))
    (ref-set animals (flatten (map #(reproduce %) @animals))))
  (add-plants)
  (dosync (ref-set day (inc @day))))


(defn animal-at [x y]
  (some (fn [animal]
          (and (= (animal :x) x)
               (= (animal :y) y)))
        @animals))

(defn plant-at [x y]
  (get @plants (gethash (list x y))))

(defn draw-world []
  (doseq [y (range height)]
    (print "|")
    (doseq [x (range width)]
      (print (cond 
               (animal-at x y) "M"
               (plant-at y x) "@"
               :else " ")))
    (println "|")))

(defn fresh-line []
  (dotimes [x (+ 2 width)] (print "-"))
  (println ""))

(defn print-stats []
  (print "day: ")
  (print @day)
  (print " | animals: ")
  (print (count @animals))
  (print " | plants: ")
  (println (count @plants)))


(defn evolution []
  (update-world)
  (fresh-line)
  (draw-world)
  (fresh-line)
  (print-stats))

(use 'clojure.test)
(def eve (first @animals))
(deftest test-reproduce
         (is (= 2 (count (reproduce eve))))
         (is (= 4 (count (flatten (map #(reproduce %) (reproduce eve))))))
         (is (= 1 (count (reproduce (assoc eve :energy (- reproduction-energy 1)))))))
