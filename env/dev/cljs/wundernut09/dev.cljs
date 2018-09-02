(ns ^:figwheel-no-load wundernut09.dev
  (:require
    [wundernut09.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
