(ns wundernut09.prod
  (:require
    [wundernut09.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
