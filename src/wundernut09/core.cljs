(ns wundernut09.core
  (:require [wundernut09.data :as d]
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
       ^{:key (str "key-" (gensym))}
       (if (zero? i)
         (square size false)
         (square size true)))]))

(defn button [value f]
  [:input {:type "button"
           :value value
           :on-click #(swap! index f)}])

(defn link [title f]
  [:p {:class "card-footer-item"
       :on-click #(swap! index f)}
   title])

;; -------------------------
;; Views

(defn home-page []
  (let [input (nth d/data @index)
        {:keys [r grid pattern rowno]} (solve/solve (d/parse-line input))]
    [:div.card
     [:header.card-header
      [:p.card-header-title.is-size-1 "Back to School, y'all"]]
     [:div.card-content
      [:p (str "Input: " input)]
      [:p (str "Pattern: " (name pattern))]
      ; [:p.is-size-7.has-text-link (str "Input: " input)]
      [:div.content
       (map row (take rowno grid))]]
      [:footer.card-footer
       (link "Previous" dec)
       (link "Next" inc)
       #_(when
         (< @index (dec (count d/data))))]]))

  ;; -------------------------
  ;; Initialize app

  (defn mount-root []
    (r/render [home-page] (.getElementById js/document "app")))

  (defn init! []
    (mount-root))
