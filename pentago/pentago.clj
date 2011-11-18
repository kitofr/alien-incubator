(def players (cycle ["X" "O"]))

(def starting-board
  [[1 2 3 4 5 6]
   [7 8 9 10 11 12]
   [13 14 15 16 17 18]
   [19 20 21 22 23 24]
   [25 26 27 28 29 30]
   [31 32 33 34 35 36]])

(defn available? [space board]
  (some (fn [brick] (some #(= %1 space) brick)) board))

(defn move [player space board]
  (letfn [(move-in-row
            ([accum row]
             (cond
               (empty? row) accum
               (= space (first row)) (concat accum (cons player (rest row)))
               true (recur (concat accum (list (first row))) (rest row))))
             ([row] (move-in-row '() row)))]
            (map move-in-row board)))

(defn space->string [space]
  (if (number? space)
    (if (< 9 space)
      (str " " space " ")
      (str "  " space " "))
    (str "  " space " ")))

(defn row->string [row] (apply str (interpose "|" (map space->string row))))
(defn board->string [board]
  (apply str (interpose "\n----+----+----+----+----+----\n" (map row->string board))))

(defn print-board [board]
  (do
    (newline)
    (newline)
    (print (board->string board))
    (newline)
    (newline)))

(print-board (move (first players) 31 starting-board))

(defn turn-corner [corner board] board)

(defn full? [board] 
  (not (some number? (mapcat identity board))))

;(defn winner? [board] false)

(defn play [board players]
  (do
    (print-board board)
    (let [player (first players)]
      (cond
        (winner? board) (print (str (first (next players)) " Wins!\n"))
        (full? board) (print "It's a Draw!\n")
        true (do
               (print (str "Select a square, " player ": "))
               (flush)
               (let [square (read)]
                 (if (and square (available? square board))
                   (recur (move player square board) (next players))
                   (recur board players))))))))

;(play starting-board players)

(defn get-row [index board]
  (nth board index))

(defn get-column [index board]
  (map #(nth %1 index) board))

(defn five-in-a-row? [seq]
  (or
    (apply = (rest seq))
    (apply = (rest (reverse seq)))))

(def indexes '(0 1 2))

(defn winner? [board]
  (or (not-every? #(= %1 false) (map #(apply = (get-row %1 board)) indexes))
      (not-every? #(= %1 false) (map #(apply = (get-column %1 board)) indexes))))

(winner? [[1 2 3]
          [1 3 3]
          [2 2 3]])


(five-in-a-row? [1 1 1 1 1 2])
(five-in-a-row? [1 2 1 1 1 2])
(five-in-a-row? [2 1 1 1 1 1])
