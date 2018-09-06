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
     ^{:key (str "key-" (gensym))} [:div {:class c}])))

(defn row [datavec]
  (let [len (count datavec)
        size (cond
               (> len 80) :tiny
               (> len 50) :small
               (< len 20) :big
               :else :normal)]
    ^{:key (str "row-" (gensym))}
    [:div.row.horizontal-flex
     (for [i datavec]
       (if (zero? i)
         (square size false)
         (square size true)))]))

(defn button [value f]
  [:input {:type "button"
           :value value
           :on-click #(swap! index f)}])

(defn link [title f can-proceed]
  (let [c (str "card-footer-item"
               (if can-proceed
                 " has-text-primary"
                 " has-text-danger"))]
    [:p {:class c
         :on-click (if can-proceed
                     #(swap! index f)
                     #())}
     title]))

;; -------------------------
;; Views

(defn home-page []
  (let [input (nth d/data @index)
        {:keys [r grid pattern rowno]} (solve/solve (d/parse-line input))]
    [:div.card
     [:header.card-header
      [:p.card-header-title.is-size-1.is-centered "Back to School, y'all"]]
     [:div.card-content.is-centered
      [:p (str "Input: " input)]
      [:p (str "Lines rendered: " rowno)]
      [:p (str "Pattern: " (name pattern))]
      [:div.content
       (map row (take rowno grid))]]
      [:footer.card-footer
       (link "Previous" dec (< 0 @index))
       (link "Next" inc (< @index (dec (count d/data))))]]))

  ;; -------------------------
  ;; Initialize app

  (defn mount-root []
    (r/render [home-page] (.getElementById js/document "app")))

  (defn init! []
    (mount-root))
