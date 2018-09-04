(ns wundernut09.core
  (:require [wundernut09.data :refer [data]]
            [wundernut09.solve :as solve]
            [reagent.core :as r]))

(def index
  (r/atom 0))

;; ----
;; UI Components

(defn square
  ([] (square :normal false))
  ([size] (square size false))
  ([size filled]
   (let [c (str "square"
                (if (= :normal size)
                  ""
                  (str " square--" (name size)))
                (when filled " has-background-grey-light"))]
     [:div {:class c}])))

(defn row [datavec]
  (let [size (cond
               (> (count datavec) 80) :tiny
               (> (count datavec) 50) :small
               (< (count datavec) 20) :big
               :else :normal)]
    [:div.row.horizontal-flex
     (for [i datavec]
       (if (zero? i)
         (square size false)
         (square size true)))]))

(defn button [value f]
  [:input {:type "button"
           :value value
           :on-click #(swap! index f)}])

;; -------------------------
;; Views

(defn home-page []
  (let [{:keys [r grid pattern rowno]} (solve/solve (nth data @index))]
    [:div [:h2 "Back to School, y'all"]
     [(case pattern
        :blinking :h3.blinking
        :vanishing :h3.hidden
        :h3)
      pattern]
     (when-not (zero? @index)
       (button "Edellinen rivi" dec))
     (when (< @index (dec (count data)))
       (button "Seuraava rivi" inc))
     (map row (take rowno grid))]))

  ;; -------------------------
  ;; Initialize app

  (defn mount-root []
    (r/render [home-page] (.getElementById js/document "app")))

  (defn init! []
    (mount-root))
