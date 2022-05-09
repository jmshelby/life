(ns life.gui
  (:require [life :as life]
            [quil.core :as q]
            [quil.middleware :as m]))

(def BOARD-SIZE 150)
(def FRAME-RATE 40)

(def BORDER 3)
(def CELL-MARGIN 0)
(def CELL-SIZE 8)
(def CELL-CORNER 5)

(defn board-start [] [5 5])

(defn aged-color [age]
  (cond
    ;; I just made this shit up ... have no idea what it will look like...
    (= age 0) [0 0   0]
    (= age 1) [0 0   200]
    (= age 2) [0 200 200]
    (= age 3) [200 100 100]
    (= age 4) [200 200 0]
    (= age 5) [255 150 0]
    (> age 5) [255 0 255]))

(defn draw-state [{:keys [state]}]
  (q/background 0)
  (let [[start-x start-y] (map (partial + BORDER) (board-start))]
    (doseq [[[x y] age] state]
      (let [x (+ start-x BORDER (* x (+ CELL-MARGIN CELL-SIZE)))
            y (+ start-y BORDER (* y (+ CELL-MARGIN CELL-SIZE)))]
        ;; (apply q/stroke (aged-color (- age 1)))
        (apply q/fill (aged-color age))
        (q/rect x y
                CELL-SIZE
                CELL-SIZE
                CELL-CORNER)))))

(defn update-state [state]
  (life/advance-board state))

(defn settings []
  ;; This appears to only work in cljs renderers so far
  (q/pixel-density 2))

(defn rand-board [h w sparsity]
  (let [sample   (* sparsity (* h w))
        rand-age #(rand-int 10)
        xs       (repeatedly #(rand-int w))
        ys       (repeatedly #(rand-int h))]
    (->> (repeatedly rand-age)
         (filter (partial < 0))
         (map vector (map vector xs ys))
         (take sample)
         (into {}))))

(defn setup []
  (q/background 10)
  ;; (q/stroke-weight (int (* SCALE WEIGHT-RATIO)))
  (q/frame-rate FRAME-RATE)
  ;; TODO - mess with camera
  ;; (q/color-mode :hsb)  ;; Set color mode to HSB (HSV) instead of default RGB.
  {:height BOARD-SIZE
   :width  BOARD-SIZE
   :state  (rand-board BOARD-SIZE BOARD-SIZE 0.5)})

(q/defsketch board
  :title "Life"
  :host "host"

  ;; TODO - auto pick size to fit board
  :size [2000 2000]
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
