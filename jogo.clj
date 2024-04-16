(require '[clojure.string :as str])

(defn unicode-str [codigo]
  (-> (Character/toChars codigo)
  String.))

;Lógica para pegar uma palavra aleátoria de um arquivo .txt e utilizar essa palavra no jogo
(defn ler-palavras [palavras]
  (with-open [rdr (clojure.java.io/reader palavras)]
    (doall (line-seq rdr))))

(defn gerador-de-palavra [palavras]
  (let [lista-de-palavras (ler-palavras palavras)]
    (rand-nth lista-de-palavras)))

(defn plataforma-De-Jogo [palavra]
  (apply str (repeat (count palavra) " _ ")))

(defn verifica-letra [letra palavra-randomica]
  (if (empty? palavra-randomica)
    false
    (do
      (if (= (str (first palavra-randomica)) (str/lower-case letra))
        true
        (verifica-letra letra (rest palavra-randomica))))))

(defn itera-plataforma [letras palavra-randomica]
  (apply str (map (fn [c] (if (some #{(str c)} letras) (str c) " _ ")) palavra-randomica)))

(defn desconta-vidas [HP letra palavra-randomica]
  (if (verifica-letra letra palavra-randomica)
        HP
       (dec HP)))

(defn -main []
  (loop []
  (println "Select the language PT or EN:")
  (def language (read-line))
  (if (= (str/lower-case language) "pt")
    (def palavra-randomica (gerador-de-palavra "palavras.txt"))
        (def palavra-randomica (gerador-de-palavra "words.txt")))

  (println "\n<<Guess the word>>")
  (println (plataforma-De-Jogo palavra-randomica))

  (loop [HP 6 letras #{}]
    (when (and (not= HP 0) (not= (itera-plataforma letras palavra-randomica) palavra-randomica))
      (println (format "\nTotal lives {%d} %s" HP (unicode-str 0x2764)))
      (println "Complete a letter: ")
      (let [letra (read-line)]
        (let [letras (conj letras (str/lower-case letra))]
          (println (itera-plataforma letras palavra-randomica))
          (if (= (itera-plataforma letras palavra-randomica) palavra-randomica)
              (println (format "\nWELL DONE! %s%s" (unicode-str 0x1F44F)(unicode-str 0x1F389))))

          (recur (desconta-vidas HP letra palavra-randomica) letras))))) ;Passando o conjunto de letras na chamada recursiva, para transitar de um estado ao outro
  (println (format "The word is: %s %s"  palavra-randomica (unicode-str 0x1F4D6)))

  (println "\nWould you like to play again? (yes or no)")
  (let [resp (read-line)]
    (if (= (str/lower-case resp) "yes")
      (recur))))
)

(-main)