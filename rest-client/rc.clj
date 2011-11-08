(ns rest-client
  (:import (java.net URL
                     HttpURLConnection)
           (java.io BufferedReader
                    InputStreamReader))
  (:refer-clojure :exclude (get)))

(defn- parse-headers [connection]
  (let [hs (.getHeaderFields connection)]
    (into {} (for [[k v] hs :when k] [(keyword (.toLowerCase k)) (seq v)]))))

(defn- parse-body [connection]
  (let [reader (BufferedReader. 
                 (InputStreamReader. 
                   (.getInputStream connection)))]
    (apply str (line-seq reader))))

(defn- parse-code [connection]
  (.getResponseCode connection))

(defn get [u]
  (let [url (URL. u)
        #^HttpURLConnection con (cast HttpURLConnection (.openConnection url))]
    (.setRequestMethod con "GET")
    {:body (parse-body con) 
     :code (parse-code con)
     :headers (parse-headers con)
     :url u
     }))

(defn post [u body]
  (let [url (URL. u)
        #^HttpURLConnection con (cast HttpURLConnection (.openConnection url))]
    (.setRequestMethod con "POST")
    
