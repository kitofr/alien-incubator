(import '(java.io BufferedReader InputStreamReader)) 
(defn cmd [p] (.. Runtime getRuntime (exec (str p)))) 
(defn cmdout [o] 
  (let [r (BufferedReader. 
             (InputStreamReader. 
               (.getInputStream o)))] 
    (dorun (map println (line-seq r))))) 
