(ns life)

(defn neighbors [{:keys [height width]} [x y]]
  ;; Currently filters out cells that are out of bounds
  ;; TODO - wrap around?
  (filter (fn [[x y]]
            (and
              (> x -1)
              (> y -1)
              (< x width)
              (< y height)))
          [[(dec x) y]
           [(inc x) y]
           [x (inc y)]
           [x (dec y)]
           [(inc x) (inc y)]
           [(dec x) (dec y)]
           [(inc x) (dec y)]
           [(dec x) (inc y)]]))

(defn advance-cell
  [{state :state :as board} coord]
  (let [;; Get Cell state
        cell (get state coord)
        ;; Count Live Neighbors
        bors (->> (neighbors board coord)
                  (keep state)
                  count)]
    (cond
      ;; Born
      (and (not cell)
           (= 3 bors))
      1
      ;; Live
      (and cell
           (or (= 2 bors)
               (= 3 bors)))
      (inc cell)
      ;; Die
      (and cell
           (not (or (= 2 bors)
                    (= 3 bors))))
      nil)))

(defn board-coords [{:keys [width height]}]
  (for [x (range width)
        y (range height)]
    [x y]))

;; Take a board, return next generation
(defn advance-board
  [board]
  (assoc board :state
         (->> board
              board-coords
              (map (juxt identity (partial advance-cell board)))
              (filter second)
              (into {}))))


(comment



  ;; Data Model
  ;; - Board
  (->> (iterate
         advance-board
         {:height 100
          :width  100
          :state  {
                   ;; [5,5] 1
                   ;; [5,4] 1
                   ;; [4,4] 1

                   [20,5]  1
                   [20,6]  1
                   [20,7]  1
                   [20,8]  1
                   [20,9]  1
                   [20,10] 1
                   [20,11] 1
                   [20,12] 1

                   }})
       (take 100))




  )
