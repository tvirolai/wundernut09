(ns wundernut09.solve)

(defn- add-padding [row]
  (let [padding '(0 0)]
    (concat padding row padding)))

(defn- fill-square? [square]
  (let [middle (nth square 2)
        filled (reduce + (concat (take 2 square) (take-last 2 square)))]
    (cond
      (and (zero? middle)
           (contains? #{2 3} filled)) true
      (and (= 1 middle)
           (contains? #{2 4} filled)) true
      :else false)))

(defn next-row [prevrow]
  (->> (add-padding prevrow)
       (partition 5 1)
       (map fill-square?)
       (mapv #(if % 1 0))))

(defn- extract-pattern [row]
  (letfn [(trim-zeros-and-rev [r]
            (->> r (drop-while zero?) reverse))]
    (->> row trim-zeros-and-rev trim-zeros-and-rev)))

(defn row-contains-pattern? [row1 row2]
  (= (extract-pattern row1)
     (extract-pattern row2)))

(defn gliding?
  ([grid] (gliding? (drop-last grid) (last grid)))
  ([grid row]
   (if (empty? grid)
     false
     (if (row-contains-pattern? (first grid) row)
       true
       (recur (rest grid)
              row)))))

(defn get-pattern [grid]
  (cond
    (> (count grid) (count (set grid))) :blinking
    (zero? (reduce + (last grid))) :vanishing
    (gliding? grid) :gliding
    (= 100 (count grid)) :other
    :else nil))

(def examples
  {:blinking [0 1 0 1 1 0 0]
   :gliding [0 1 0 1 1 1 0 0]
   :vanishing [0 1 0 1 0]})

(defn build-grid [row]
  (take 100 (iterate next-row row)))

(defn solve
  ([row] (solve row (build-grid row) 1))
  ([row grid rowno]
   (let [res (get-pattern (take rowno grid))]
     (if (keyword? res)
       res
       (recur row
              grid
              (inc rowno))))))
