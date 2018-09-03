(ns wundernut09.core
  (:require [wundernut09.data :refer [data]]
            [wundernut09.solve :as solve]
            [reagent.core :as r]))

(def index
  (r/atom 0))

;; ----
;; UI Components

(def square
  [:div.square])

(def filled-square
  [:div.square.has-background-grey-light])

(defn row [datavec]
  [:div.row.horizontal-flex
    (for [i datavec]
      (if (zero? i)
        square
        filled-square))])

(defn button [value direction]
  [:input {:type "button"
           :value value
           :on-click #(swap! index (if (= :inc direction)
                                     inc
                                     dec))}])

;; -------------------------
;; Views

(defn home-page []
  (let [{:keys [r grid pattern rowno]} (solve/solve (nth data @index))]
    [:div [:h2 "Back to School, y'all"]
     [:h3 pattern]
     (when-not (zero? @index)
       (button "Edellinen rivi" :dec))
     (when (<= @index (count data))
       (button "Seuraava rivi" :inc))
     (map row (take rowno grid))]))

  ;; -------------------------
  ;; Initialize app

  (defn mount-root []
    (r/render [home-page] (.getElementById js/document "app")))

  (defn init! []
    (mount-root))
