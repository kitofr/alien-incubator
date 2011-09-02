(ns rest-client
  (:import (java.net URL
                     HttpURLConnection)
           (java.io BufferedReader
                    InputStreamReader))
  (:refer-clojure :exclude (get)))

(defn get [u]
  (let [u (URL. u)
        #^HttpURLConnection con (cast HttpURLConnection (.openConnection u))
        reader (BufferedReader. 
                 (InputStreamReader. 
                   (.getInputStream con)))]
  (apply str (line-seq reader))))

;(defn get [url]
  ;(let [url (URL. url)]
    ;(with-open [stream (. url (openStream))]
      ;(let [buf (BufferedReader. 
                 ;(InputStreamReader. stream))]
    ;(apply str (line-seq buf))))))
