(apply str (loop [del "," coll ["foo" "bar"] res []]
  (if (empty? coll)
    res
    (recur del (rest coll) (conj res (cons (first coll) del))))))
