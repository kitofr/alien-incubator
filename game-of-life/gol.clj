(def width 100)
(def height 30)
(def jungle '(45 10 10 10))
(def plant-energy 80)
(def plants (ref {}))

(defn random-plant [left top width height]
  (let [pos (list (+ left (rand-int width)) (list (+ top (rand-int height))))]
    (dosync (alter plants assoc (.hashCode pos) true))))

(defn add-plants []
  (apply #'random-plant jungle)
  (random-plant 0 0 width height))

(defstruct animal :x :y :energy :dir :genes)

(def animals (ref
  (list (struct-map animal 
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
    (when plants (.hashCode pos))
    (+ (animal :energy) plant-energy)
    (pos plants :none)))

(def reproduction-energy 200)

(defn reproduce [animal]
  (when (>= (animal :energy) reproduction-energy)
    (let [genes     (animal :genes)
          mutation  (rand-int 8)]
      (assoc animal :energy (int (/ e 2))
                    :genes mutation (max 1 (+ (nth genes mutation) (rand-int 3) -1))
                    ))))
;  (assoc (animal-nu :genes) genes)
;  (push animal-nu *animals*)))))

;;(defun update-world ()
;;  (setf *animals* (remove-if (lambda (animal)
;;                                 (<= (animal-energy animal) 0))
;;                             *animals*))
;;  (mapc (lambda (animal)
;;          (turn animal)
;;          (move animal)
;;          (eat animal)
;;          (reproduce animal))
;;        *animals*)
;;  (add-plants))
;;
;; (defn draw-world []
;;  (loop for y
;;        below *height*
;;        do (progn (fresh-line)
;;                  (princ "|")
;;                  (loop for x
;;                        below *width*
;;                        do (princ (cond ((some (lambda (animal)
;;                                                 (and (= (animal-x animal) x)
;;                                                      (= (animal-y animal) y)))
;;                                               *animals*)
;;                                         #\M)
;;                                        ((gethash (cons x y) *plants*) #\*)
;;                                         (t #\space))))
;;                  (princ "|"))))
;;
;;(defun evolution ()
;;  (draw-world)
;;  (fresh-line)
;;  (let ((str (read-line)))
;;    (cond ((equal str "quit") ())
;;          (t (let ((x (parse-integer str :junk-allowed t)))
;;               (if x
;;                   (loop for i
;;                      below x
;;                      do (update-world)
;;                      if (zerop (mod i 1000))
;;                      do (princ #\.))
;;                   (update-world))
;;               (evolution))))))
