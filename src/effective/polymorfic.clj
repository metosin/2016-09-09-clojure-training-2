(ns effective.polymorfic)

;;
;; Animals as data: Cow, Elephant, Frog
;;

(def cow {::type :cow :name "moo'er"})
(def duck {::type :duck, :name "quack'er"})
(def frog {::type :frog, :name "croak'er"})

;; say - multimethod on ::type

;; implement them for duck and frog

;; what does the frog say?

;; what does the duck say?

;; what does the cow say?

;; implement default to nil

;; what does the cow say now?

;;
;; Protocols
;;

(defprotocol Flyer
  (fly [this distance]))

;;
;; Animals as records: Elephant, Bird
;;

(defrecord Elephant [name])

(defrecord Bird [name]
  Flyer
  (fly [_ distance] (format "%s flew %s km" name distance)))

(def elephant (->Elephant "toot'er"))
(def bird (->Bird "tweet'er"))

;; can the elephant fly?

;; make the bird fly

;; Well, Elephants should be able to fly too

;; fly the elephant


;;
;; Keyword hierarchies
;;

;; ::designer is ::happy
;; ::ui-designer is ::designer
;; ::ux-designer is ::designer
;; ::programmer is ::happy
;; ::boss is

;; who is ::happy?

;; who are ::designers?

;; is ::ux-designer ::happy?

