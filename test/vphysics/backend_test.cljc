(ns vphysics.backend-test
  (:require [clojure.test :refer [deftest is]]
            [kotoba.physics.contract :as contract]
            [vphysics.backend :as backend]))

(deftest solves-shared-reduced-order-case-with-explicit-qualification
  (let [scene (contract/make-scene {:id :vehicle :dimensions 3 :entities []})
        case (contract/make-case {:id :range :scene scene :domain :vehicle
                                  :backend-kind backend/backend-id :fidelity :reduced-order
                                  :controls {:mass-kg 1800.0 :regen-credit 0.15 :aux-power-w 500.0
                                             :body {:crr 0.01 :cd 0.28 :frontal-area 2.2
                                                    :avg-speed 22.0}}})
        result (contract/solve backend/backend case)]
    (is (pos? (get-in result [:result/fields :total-J-per-km])))
    (is (contract/qualified? result :execution))
    (is (not (contract/qualified? result :model-validation)))
    (is (contract/supports? backend/backend :reduced-order #{:vehicle-road-load}))))
