(def players (cycle ["X" "O"]))

(def starting-board
  [[11 12 13 14]
   [21 22 23 24]
   [31 32 33 34]
   [41 42 43 44]])

(defn available? [space board]
  (some (fn [brick] (some #(= %1 space) brick)) board))
