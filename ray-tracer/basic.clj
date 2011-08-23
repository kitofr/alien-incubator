(ns basic
  (:import
    (javax.swing JFrame JPanel)
    (java.awt.image BufferedImage)))

(defn square [x] (* x x))

(defn mag [x y z]
  (Math/sqrt (+ (square x) (square y) (square z))))

(defstruct point :x :y :z)
(defn defpoint [x y z]
  (struct point x y z))

(defn point-subtract [p1 p2]
  (defpoint 
    (- (:x p1) (:x p2))
    (- (:y p1) (:y p2))
    (- (:z p1) (:z p2))))

(defn unit-vector [p]
  (let [x (:x p)
        y (:y p)
        z (:z p)
        d (mag x y z)]
    (defpoint (/ x d) (/ y d) (/ z d))))

(defn distance [p1 p2]
  (mag (- (:x p1) (:x p2))
       (- (:y p1) (:y p2))
       (- (:z p1) (:z p2))))

(defn minroot [a b c]
  (if (zero? a)
    (/ (- c) b)
    (let [disc (- (square b) (* 4 a c))]
      (if (> disc 0)
        (let [discrt (Math/sqrt disc)]
          (min (/ (+ (- b) discrt) (* 2 a))
               (/ (- (- b) discrt) (* 2 a))))))))

(def eye (defpoint 150 150 200))

(defstruct sphere :color :radius :centre)

(defn defsphere [point r c]
  (struct sphere c r point))

; constant world for now
(def world [(defsphere (defpoint 250 150 -700) 150 0.52)
            (defsphere (defpoint 225 270 -600) 180 1.0)
            (defsphere (defpoint 425 270 -200) 80 0.99)
            (defsphere (defpoint 225 370 -400) 80 0.73)
            (defsphere (defpoint 275 175 -100) 20 0.64)])

(defn sphere-normal [s pt]
  (let [c (:centre s)]
    (unit-vector (point-subtract c pt))))

(defn sphere-intersect [s pt ray]
  (let [c (:centre s)
        n (minroot (+ (square (:x ray)) (square (:y ray)) (square (:z ray)))
                   (* 2 (+ (* (- (:x pt) (:x c)) (:x ray))
                           (* (- (:y pt) (:y c)) (:y ray))
                           (* (- (:z pt) (:z c)) (:z ray))))
                   (+ (square (- (:x pt) (:x c)))
                      (square (- (:y pt) (:y c)))
                      (square (- (:z pt) (:z c)))
                      (- (square (:radius s)))))]
    (if n
      (defpoint (+ (:x pt) (* n (:x ray)))
                (+ (:y pt) (* n (:y ray)))
                (+ (:z pt) (* n (:z ray)))))))

(defn lambert [s intersection ray]
  (let [normal (sphere-normal s intersection)]
    (max 0 (+ (* (:x ray) (:x normal))
              (* (:y ray) (:y normal))
              (* (:z ray) (:z normal))))))

;; second item = what we hit
;; first item = where we hit
(defn first-hit [pt ray]
  (reduce 
    (fn [x y]
      (let [hx (first x) hy (first y)]
        (cond
          (nil? hx) y
          (nil? hy) x
          :else (let [d1 (distance hx pt) d2 (distance hy pt)]
                  (if (< d1 d2) x y)))))
    (map (fn [obj]
           (let [h (sphere-intersect obj pt ray)]
             (if (not (nil? h))
               [h obj]))) world)))

(defmacro unless [condition & body]
    `(if (not ~condition)
       ~@body))

(defn send-ray [src ray]
  (let [hit (first-hit src ray)]
    (unless (nil? hit)
      (let [i (first hit) s (second hit)]
        (* (lambert s ray i) (:color s)))
      0)))

(defn color-at [x y]
  (let [ray (unit-vector (point-subtract (defpoint x y 0) eye))]
    (* (send-ray eye ray) 255)))

(defn set-pixel [image x y color]
  (.setRGB image x y color))

(defn ray-trace [image w h]
  (doseq [x (range 1 w)]
    (doseq [y (range 1 h)]
      (set-pixel image x y (color-at x y))))
  image)

(defn tracer [w h]
  (doseq [x (range 1 w)]
    (doseq [y (range 1 h)]
      (let [color (color-at x y)]
        (when (< 0 color)
          (printf "[(%s,%s)=>%s]," x y (color-at x y) " "))))))

(defn draw [g image]
  (.drawImage g image 0 0 Color/RED nil))

(def canvas (proxy [JPanel] []
              (paintComponent [g]
                              (proxy-super paintComponent g)    
                              (let [w (.getWidth this)
                                    h (.getHeight this)
                                    image (BufferedImage. w h BufferedImage/TYPE_BYTE_GRAY)]
                                (draw g (ray-trace image w h))))))
(defn jframe []
  (let [frame (JFrame. "Ray Tracing")]
    (doto frame
      (.add canvas)
      (.setSize 500 300)
      (.setResizable false)
      (.setVisible true))))
