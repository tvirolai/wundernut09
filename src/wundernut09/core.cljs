(ns wundernut09.core
  (:require [wundernut09.data :refer [data]]
            [wundernut09.solve :as solve]
            [reagent.core :as r]))

(def state
  (let [{:keys [row grid pattern rowno]} (solve/solve (first data))]
    (r/atom {:row row
             :grid (take rowno grid)
             :pattern pattern})))

;; ----
;; UI Components

(def square
  [:div.square])

(def filled-square
  [:div.square.has-background-grey-light])

(defn row [datavec]
  [:div.row.horizontal-flex
    (for [i datavec]
      ^{:key (str i "kee" (rand-int 100))}
        (if (zero? i)
          square
          filled-square))])


;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Back to School, y'all"]
   [:h3 (:rowno @state)]
   [:h3 (:pattern @state)]
   (map row (:grid @state))
   #_(row (:row @state))])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
