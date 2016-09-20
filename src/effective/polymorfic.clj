(ns effective.polymorfic)

;;
;; Animals as data: Cow, Elephant, Frog
;;

(def cow {::type :cow :name "moo'er"})
(def duck {::type :duck, :name "quack'er"})
(def frog {::type :frog, :name "croak'er"})

;; say - multimethod on ::type

(defmulti say ::type)

;; implement them for duck and frog

(defmethod say :duck [{:keys [name]}]
  (format "duck %s says quack" name))

(defmethod say :frog [{:keys [name]}]
  (format "forg %s says croak" name))

;; what does the frog say?

(say frog)

;; what does the duck say?

(say duck)

;; what does the cow say?

#_(say cow)

;; implement default to nil

(defmethod say :default [_] nil)

(clojure.pprint/pprint
  (methods say))

;; what does the cow say now?

(say cow)

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

(satisfies? Flyer elephant)
(satisfies? Flyer bird)

;; make the bird fly

(fly bird 12)

;; Well, Elephants should be able to fly too

(extend-protocol Flyer
  Elephant
  (fly [{:keys [name]} distance]
    (format "elepahnt %s flew %s meters" name distance))

  String
  (fly [_ _] "String flew away the coocoo's nest!"))

;; fly the elephant

(fly elephant 2)

(fly "kikka" 1)

;;
;; Keyword hierarchies
;;

;; ::designer is ::happy
;; ::ui-designer is ::designer
;; ::ux-designer is ::designer
;; ::programmer is ::happy
;; ::boss is

(derive ::designer ::happy)
(derive ::ui-designer ::designer)
(derive ::ux-designer ::designer)
(derive ::programmer ::happy)

;; who is ::happy?

(descendants ::happy)

;; who are ::designers?

(descendants ::designer)

;; is ::ux-designer ::happy?

(isa? ::ux-designer ::happy)

(derive String ::happy)

(isa? String ::happy)
