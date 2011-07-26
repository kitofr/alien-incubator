(System.Reflection.Assembly/Load "System.Xml,  
    Version=2.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089")  
(import '(System.Xml XmlDocument))  
  
(defn getStatus[userName]   
    (let [url (str "http://twitter.com/users/" userName ".xml")  
          xml (doto (XmlDocument.) (. Load url))]  
      (map #(.InnerText %) (. xml SelectNodes "/user/status/text"))))  

(println (getStatus "kitofr"))  
