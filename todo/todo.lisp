(defun make-todo (subject prio estimate)
  (list :subject subject :prio prio :estimate estimate))

(defvar *db* nil)
(defun add-todo (todo) (push todo *db*))

(defun dump-db ()
  (dolist (todo *db*)
    (format t "~{~a:~10t~a~%~}~%" todo)))

(defun prompt-read (prompt)
  (format *query-io* "~a: " prompt)
  (force-output *query-io*)
  (read-line *query-io*))

(defun prompt-for-todo ()
  (make-todo
    (prompt-read "Subject")
    (or (parse-integer (prompt-read "Priority [1-5]") :junk-allowed t) 0 )
    (or (parse-integer (prompt-read "Estimate [1,2,4,8]") :junk-allowed t) 0)))

(defun add-todos ()
  (loop (add-todo (prompt-for-todo))
        (if (not (y-or-n-p "Another? [y/n]:")) (return))))

(defun save-db (filename)
  (with-open-file (out filename
                       :direction :output
                       :if-exists :supersede)
    (with-standard-io-syntax
      (print *db* out))))

(defun load-db (filename)
  (with-open-file (in filename)
    (with-standard-io-syntax
      (setf *db* (read in)))))

(defun select (selector-fn)
  (remove-if-not selector-fn *db*))

(defun where (&key subject prio estimate)
  #'(lambda (todo)
      (and
        (if subject   (equal (getf todo :subject)   subject)  t)
        (if prio      (equal (getf todo :prio)      prio)     t)
        (if estimate  (equal (getf todo :estimate)  estimate) t))))
