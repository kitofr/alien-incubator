(import '(System.Reflection Assembly))
(Assembly/LoadWithPartialName "System.Xml")
(import '(System.Xml XmlDocument))
  
(defn getStatus[userName]   
    (let [url (str "http://twitter.com/users/" userName ".xml")  
          xml (doto (XmlDocument.) (. Load url))]  
      (map #(.InnerText %) (. xml SelectNodes "/user/status/text"))))  

(println (getStatus "kitofr"))  
