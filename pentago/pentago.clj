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


(defn full? [board]
  (not (some number? (mapcat identity board))))

;(defn winner? [board] false)

(defn get-row [index board]
  (nth board index))

(defn get-column [index board]
  (map #(nth %1 index) board))

(defn five-in-a-row? [sq]
  (or
    (apply = (rest sq))
    (apply = (rest (reverse sq)))))

(def indexes '(0 1 2 3 4 5))

(defn get-indexes [sq board]
  (map #(get-row (first %1) (get-column (second %1) board)) sq))

(map five-in-a-row? 
     (get-indexes [[0 0] [1 1] [2 2] [3 3] [4 4] [5 5]] starting-board))

;(let bind2nd f y = fun x -> f x y)
(defn winner? [board]
  (or (some #(= true %1) (map five-in-a-row? (map #(get-row %1 board) indexes)))
      (some #(= true %1) (map five-in-a-row? (map #(get-column %1 board) indexes)))
      (five-in-a-row? (get-indexes [[0 0] [1 1] [2 2] [3 3] [4 4] [5 5]] board))
      (five-in-a-row? (get-indexes [[0 5] [1 4] [2 3] [3 2] [4 1] [5 0]] board))
      (apply = (get-indexes [[1 0] [2 1] [3 2] [4 3] [5 4]] board))
      (apply = (get-indexes [[0 1] [1 2] [2 3] [3 4] [4 5]] board))
      (apply = (get-indexes [[0 4] [1 3] [2 2] [3 1] [4 0]] board))
      (apply = (get-indexes [[1 5] [2 4] [3 3] [4 2] [5 1]] board))))

(defn play [board players]
  (do
    (print-board board)
    (let [player (first players)]
      (cond
        (winner? board) (print (str (first "*** Player: " (next players)) " Wins! ***\n"))
        (full? board) (print "It's a Draw!\n")
        true (do
               (print (str "Select a square, " player ": "))
               (flush)
               (let [square (read)]
                 (if (and square (available? square board))
                   (recur (move player square board) (next players))
                   (recur board players))))))))




(def board
  [[\X 2 3 4 5 6]
   [7 \O 9 10 11 12]
   [13 14 \X 16 17 18]
   [19 20 21 22 23 24]
   [25 26 27 28 29 30]
   [31 32 33 34 35 36]])


(def A [[0 0] [0 1] [0 2] 
        [1 0] [1 1] [1 2]
        [2 0] [2 1] [2 2]])
(def B [[0 3] [0 4] [0 5] 
        [1 3] [1 4] [1 5]
        [2 3] [2 4] [2 5]])
(def C [[3 0] [3 1] [3 2] 
        [4 0] [4 1] [4 2]
        [5 0] [5 1] [5 2]])
(def D [[3 3] [3 4] [3 5] 
        [4 3] [4 4] [4 5]
        [5 3] [5 4] [5 5]])

(defn get-corner [indexes board]
  (get-indexes indexes board))

(defn turn-corner [corner dir]
  (let [[a b c d e f g h i] corner] dir
    (if (= 0 dir)
      (list g d a h e b i f c)
      (list c f i b e h a d g))))

(defn turn [corner dir board] board)

(get-corner A board)
(turn-corner (get-corner A board) 1)

;(play starting-board players)
