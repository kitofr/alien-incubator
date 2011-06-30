(def *width* 100)
(def *height* 30)
(def *jungle* '(45 10 10 10))
(def *plant-energy* 80)

(def *plants* #{})

(defn random-plant [left top width height]
  (let [pos (cons (+ left (rand-int width)) (list (+ top (rand-int height))))]
        (conj *plants* (.hashCode pos))))

(defn add-plants []
  (apply #'random-plant *jungle*)
  (random-plant 0 0 *width* *height*))

(defstruct animal :x :y :energy :dir :genes)

;;(def *animals*
;;  (list (struct-map :x (ash *width* -1)
;;                    :y (ash *height* -1)
;;                    :energy 1000
;;                    :dir 0
;;                    :genes (loop repeat 8
;;                             collecting (1+ (rand-int 10))))))
;;
