(defn sq [x] (* x x))

(defn mag [x y z]
  (Math/sqrt (+ (sq x) (sq y) (sq z))))

(defstruct point :x :y :z)

(defn unit-vector [x y z]
  (let [d (mag x y z)]
    (struct point (/ x d) (/ y d) (/ z d))))

(defn distance [p1 p2]
  (mag (- (:x p1) (:x p2))
       (- (:y p1) (:y p2))
       (- (:z p1) (:z p2))))

(defn minroot [a b c]
  (if (zero? a)
    (/ (- c) b)
    (let [disc (- (sq b) (* 4 a c))]
      (if (> disc 0)
              (let [discrt (Math/sqrt disc)]
                (min (/ (+ (- b) discrt) (* 2 a))
                     (/ (- (- b) discrt) (* 2 a))))))))
