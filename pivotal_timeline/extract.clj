(import '(System.Reflection Assembly))
(Assembly/LoadWithPartialName "System.Xml")
(import '(System.Xml XmlDocument))
(import '(System DateTime))

(def yaml "\ntype: %s\nname: %s\ncreated at: %s\naccepted at: %s\n")
(def csv "%s;%s;%s;%s\n")
(def types '("release" "feature" "bug"))
(def xml (doto (XmlDocument.) (. Load "11621.xml")))

(defn find-by [story-type]
  (let [criteria (format "/stories/story[story_type='%s'][current_state='accepted']" story-type)]
    (map #(.InnerText %) 
         (. xml SelectNodes 
            (format "%1$s/name | %1$s/created_at | %1$s/accepted_at | %1$s/story_type " criteria)))))

(defn to-date [date]
  (DateTime/Parse (subs date 0 19)))

(defn format-node [node frmt]
  (let [story-type (nth node 0)
        story-name (nth node 1)
        created-at (to-date (nth node 2))
        accepted-at (to-date (nth node 3))]        
    (format frmt story-type story-name created-at accepted-at)))

(defn format-all [nodes frmt]
  (map #(format-node % frmt) (partition 4 nodes)))

(defn to-csv []
  (println "type;name;created-at;accepted-at;")
  (doseq [t types] (println (print-all (find-by t) csv))))

(defn to-yaml []
  (doseq [t types] (println (print-all (find-by t) yaml))))
