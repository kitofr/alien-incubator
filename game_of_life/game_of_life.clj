(def width 100)
(def height 30)
(def jungle '(45 10 10 10))
(def plant-energy 80)
(def plants #{})

(defn random-plant [left top width height]
  (let [pos (cons (+ left (rand-int width)) (list (+ top (rand-int height))))]
        (conj plants (.hashCode pos))))

(defn add-plants []
  (apply #'random-plant jungle)
  (random-plant 0 0 width height))

(defstruct animal :x :y :energy :dir :genes)

(def animals
  (list (struct-map animal 
                    :x 50;;(/ 2 width)
                    :y 15;;(/ 2 height)
                    :energy 1000
                    :dir 0
                    :genes (take 8
                             (repeatedly #(rand-int 10))))))

(defn move [animal]
  (let [dir (:dir animal)
        x (:x animal)
        y (:y animal)]
    (assoc animal :x (mod (+ x
                             (cond 
                                (and (>= dir 2) (< dir 5)) 1
                                (or (= dir 1) (= dir 5)) 0
                                :else -1)
                                   width)
                             width))
    (assoc animal :y (mod (+ y
                             (cond (and (>= dir 0) (< dir 3)) -1
                                   (and (>= dir 4) (< dir 7)) 1
                                   :else 0)
                             height)
                          height))
    (dec (:energy animal))))

      
                                      
;;(defun move (animal)
;;  (let ((dir (animal-dir animal))
;;        (x (animal-x animal))
;;        (y (animal-y animal)))
;;    (setf (animal-x animal) (mod (+ x
;;                                    (cond ((and (>= dir 2) (< dir 5)) 1)
;;                                          ((or (= dir 1) (= dir 5)) 0)
;;                                          (t -1))
;;                                    *width*)
;;                                 *width*))
;;    (setf (animal-y animal) (mod (+ y
;;                                    (cond ((and (>= dir 0) (< dir 3)) -1)
;;                                          ((and (>= dir 4) (< dir 7)) 1)
;;                                          (t 0))
;;                                    *height*)
;;                                 *height*))
;;    (decf (animal-energy animal))))
;;
