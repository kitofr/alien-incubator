(defparameter *num-players* 2)
(defparameter *square-size* 4)
(defparameter *win-by* (- 1 *square-size*))
(defparameter *board-size* (* 4 *square-size*))

(defun board-array (lst)
  (make-array *board-size* :initial-contents lst))

(defun gen-board ()
  (board-array (loop for n below *board-size*
                     collect 0)))

(defun player-letter (n)
  (if (eq n 0)
    '\.
    (code-char (+ 96 n))))

(defun draw-board (board)
  (loop for y below *square-size*
        do (progn (fresh-line)
                  (loop for x below *square-size*
                        for slot = (aref board (+ x (* *square-size* y)))
                        do (format t "~a " (player-letter slot))))))

(defun game-tree (board player square direction pos)
  (list player
        board
        (rotate-move square
                     direction
                     player
                     (add-ball-move board player pos))))

(defun rotate-square (square direction player board)
  (game-tree (board player)))

(defun point2pos (x y)
  (+ (mod x *square-size*) (* y *square-size*)))

(defun add-ball (board player pos)
  (setf b (make-array *board-size* :initial-contents board))
  (setf (aref b pos) player)
  b)

(defun available-slots (board)
  (loop for i below *board-size*
      if (eq 0 (aref board i))
      append (list i)))
