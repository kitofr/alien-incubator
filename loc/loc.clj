(use '[clojure.contrib.duck-streams :only (reader)])
(use '[clojure.contrib.str-utils :only (str-join)])

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

(def exts '(".cs" ".vb" ".vbs" ".rb" ".fs" ".fsx"))
(defn count-loc [base-file]
  (cons (.toString (java.util.Date.)) 
        (map #(loc (java.io.File. base-file) %) exts)))

(defn format-output []
  (let [headers (cons "date" exts)]
    (str-join ";" headers)))

