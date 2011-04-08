(use '[clojure.contrib.duck-streams :only (reader)])

(defn non-blank? [line] (if (re-find #"\S" line) true false))
(defn non-svn? [file] (not (.contains (.toString file) ".svn")))
(defn cs-source? [file] (.endsWith (.toString file) ".cs"))
(defn cs-loc [base-file]
  (reduce
    +
    (for [file (file-seq base-file)
          :when (and (cs-source? file) (non-svn? file))]
      (with-open [rdr (read-file)
                  (count (filter non-blank? (line-seq rdr)))))))

