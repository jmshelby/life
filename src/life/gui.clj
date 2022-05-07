(ns life.gui
  (:require [life :as life]
            [quil.core :as q]
            [quil.middleware :as m]))

(def BORDER 10)
(def CELL-MARGIN 1)

(def CELL-SIZE 15)

(def CELL-CORNER 3)

(defn board-start [] [10 10])

(defn aged-color [age]
  (cond
    ;; I just made this shit up ... have no idea what it will look like...
    (= age 1) [0 0   200]
    (= age 2) [0 200 200]
    (= age 3) [200 100 100]
    (= age 4) [200 200 0]
    (= age 5) [255 150 0]
    (> age 5) [255 0 255]
    )
  )

(defn draw-state [{:keys [state]}]
  (println "draw state " state)
  (q/background 10)
  (let [[start-x start-y] (map (partial + BORDER) (board-start))]
    (doseq [[[x y] age] state]
      (let [x (+ start-x BORDER (* x (+ CELL-MARGIN CELL-SIZE)))
            y (+ start-y BORDER (* y (+ CELL-MARGIN CELL-SIZE)))]
        (apply q/fill (aged-color age))
        (q/rect x y
                CELL-SIZE
                CELL-SIZE
                CELL-CORNER)
        )
      )
    )
  )

(defn update-state [state]
  (println "update state")
  (life/advance-board state)
  )

(defn settings []
  ;; This appears to only work in cljs renderers so far
  (q/pixel-density 2))

(defn setup []
  (q/background 10)
  ;; (q/stroke-weight (int (* SCALE WEIGHT-RATIO)))
  (q/frame-rate 5)
  ;; TODO - mess with camera
  ;; (q/color-mode :hsb)  ;; Set color mode to HSB (HSV) instead of default RGB.

  {:height 100
   :width  100
   :state  {
            [5,5] 1
            [5,4] 1
            [4,4] 1

            [20,4]  1
            [20,5]  1
            [20,6]  1
            [20,7]  1
            ;; [20,8]  1
            ;; [20,9]  1
            [20,10] 1
            [20,11] 1
            [20,12] 1
            [20,13] 1
            [20,14] 1
            [20,15] 1
            [20,16] 1

            [20,17] 1
            [20,18] 1
            [20,19] 1 [21,19] 1 [22,19] 1 [23,19] 1



            [2,3] 1
            [2,4] 1
            [2,5] 1
            [2,6] 1
            [2,7] 1


            }}
  )

(q/defsketch board
  :title "Life"
  :host "host"

  :size [1000 1000]
  :features [
             ;; :keep-on-top
             :resizable
             ]

  ;; If you want full screen
  ;; :size :fullscreen
  ;; :features [:present]

  ;; setup function called only once, during sketch initialization.
  :setup setup
  :settings settings

  ;; update-state is called on each iteration before draw-state.
  :update life/advance-board
  :draw draw-state

  ;; This sketch uses functional-mode middleware.
  ;; Check quil wiki for more info about middlewares and particularly
  ;; fun-mode.
  :middleware [m/fun-mode])

(defn -main [& _]
  (println "I guess it just starts when evaling??")
  )

;;
