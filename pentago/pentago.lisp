(defparameter *num-players* 2)
(defparameter *square-size* 4)
(defparameter *win-by* (- 1 *square-size*))
(defparameter *board-size* (* 4 *square-size*))

(defun board-array (lst)
  (make-array *board-size* :initial-contents lst))

(defun gen-board ()
  (board-array (loop for n below *board-size*
                     collect (list 0))))

(defun player-letter (n)
  (if (eq n 0)
    '\.
    (code-char (+ 96 n))))

(defun draw-board (board)
  (loop for y below *square-size*
        do (progn (fresh-line)
                  (loop for x below *square-size*
                        for slot = (aref board (+ x (* *square-size* y)))
                        do (format t "~a " (player-letter (first slot)))))))
