(ns life)

(defn *neighbors-bounded [{:keys [height width]} [x y]]
  ;; Filters out cells that are out of bounds
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

(defn *wrap [max point]
  (cond
    ;; Wrap to the end
    (> 0 point)    (dec max)
    ;; Wrap to the begining
    (>= point max) 0
    ;; In bounds
    :else          point))

(def wrap (memoize *wrap))

(defn *neighbors-wrapped
  [{:keys [height width]} [x y]]
  (->> ;; Each of the neighbors
    [[(dec x) y]
     [(inc x) y]
     [x (inc y)]
     [x (dec y)]
     [(inc x) (inc y)]
     [(dec x) (dec y)]
     [(inc x) (dec y)]
     [(dec x) (inc y)]]
    ;; Wrap around to the other side
    (map (fn [[x y]]
           [(wrap width x)
            (wrap height y)]))))

(def neighbors (memoize *neighbors-wrapped))

(defn advance-cell
  [{state :state :as board} coord]
  (let [;; Get Cell state
        cell (get state coord)
        ;; Count Live Neighbors
        bors (->> coord
                  (neighbors (select-keys board [:height :width]) )
                  (keep state)
                  count)]
    (cond
      ;; Born
      (and (not cell)
           (or
             ;; (= 2 bors)
             (= 3 bors)
             ;; (= 4 bors)
             )
           )
      1

      ;; Live
      (and cell
           (or
             (= 2 bors)
             (= 3 bors)
             ;; (= 4 bors)
             ;; (= 5 bors)
             )
           ;; Random ability to die
           (< (rand) 0.999)
           )
      (inc cell)

      ;; Random ability to live (with over-population)
      ;; (and cell
      ;;      (= 4 bors)
      ;;      (= 5 bors)
      ;;      (> (rand) 0.99)
      ;;      )
      ;; (inc cell)

      ;; Random ability to spawn
      (and (not cell)
           (> (rand) 0.99999))
      1

      ;; Random ability to spawn, better when next to someone
      (and (not cell)
           (< 0 bors)
           (> (rand) 0.999))
      1

      ;; Die
      :else
      nil)))


(defn *board-coords [width height]
  (for [x (range width)
        y (range height)]
    [x y]))

(def board-coords (memoize *board-coords))

;; Take a board, return next generation
(defn advance-board
  [{:keys [width height] :as board}]
  (assoc board :state
         (->> (board-coords width height)
              (pmap (juxt identity (partial advance-cell board)))
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
