(use '[clojure.contrib.duck-streams :only (reader)])

(defn non-blank? [line] (if (re-find #"\S" line) true false))
(defn non-svn? [file] (not (.contains (.toString file) ".svn")))
(defn source? [file, ext] (and (not (.contains (.toString file) "designer")) (.endsWith (.toString file) ext)))

(defn loc [base-file ext]
  (reduce
    +
    (for [file (file-seq base-file)
          :when (and (source? file ext) (non-svn? file))]
      (with-open [rdr (reader file)]
        (count (filter non-blank? (line-seq rdr)))))))

(defn count-loc [base-file]
  (cons (.toString (java.util.Date.)) 
        (cons (loc (java.io.File. base-file) ".rb") 
              (cons (loc (java.io.File. base-file) ".cs") 
                    (cons (loc (java.io.File. base-file) ".vb") ())))))
