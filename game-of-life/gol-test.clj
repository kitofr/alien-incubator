(ns gol-tests
  (:require clojure.test))

(deftest reproduce
  (is (= 2 (count (reproduce {:energy 1000 :genes '(0 0 0 0 0 0 0 0 0)})))))

