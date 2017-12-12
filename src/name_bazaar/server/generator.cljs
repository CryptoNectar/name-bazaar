(ns name-bazaar.server.generator
  (:require
    [cljs-time.coerce :refer [to-epoch]]
    [cljs-time.core :as t]
    [cljs-web3.core :as web3]
    [district.server.config.core :refer [config]]
    [district0x.shared.utils :refer [rand-str rand-nth-except]]
    [district.server.web3.core :refer [my-addresses]]
    [mount.core :as mount :refer [defstate]]
    [name-bazaar.server.contracts-api.auction-offering :as auction-offering]
    [name-bazaar.server.contracts-api.auction-offering-factory :as auction-offering-factory]
    [name-bazaar.server.contracts-api.buy-now-offering :as buy-now-offering]
    [name-bazaar.server.contracts-api.buy-now-offering-factory :as buy-now-offering-factory]
    [name-bazaar.server.contracts-api.ens :as ens]
    [name-bazaar.server.contracts-api.offering-registry :as offering-registry]
    [name-bazaar.server.contracts-api.offering-requests :as offering-requests]
    [district.server.web3.core :as dweb3]
    [name-bazaar.server.contracts-api.registrar :as registrar]
    [name-bazaar.server.deployer]
    [cljs.nodejs :as nodejs]
    [cljs-web3.eth :as web3-eth]
    ))


(def namehash (aget (js/require "eth-ens-namehash") "hash"))
(def normalize (aget (js/require "eth-ens-namehash") "normalize"))
(def sha3 (comp (partial str "0x") (aget (js/require "js-sha3") "keccak_256")))

(declare start)
(defstate ^{:on-reload :noop} generator :start (start (merge (:generator @config)
                                                             (:generator (mount/args)))))

(defn start [{:keys [:total-accounts :offerings-per-account :offering/type]}]
  (dotimes [address-index total-accounts]
    (dotimes [_ offerings-per-account]
      (let [owner (nth (my-addresses) address-index)
            label (normalize (rand-str (+ (rand-int 7) 3)))
            name (str label "." registrar/root-node)
            node (namehash name)
            offering-type (or type (rand-nth [:buy-now-offering :auction-offering]))
            price (web3/to-wei (/ (inc (rand-int 10)) 10) :ether)
            buyer (rand-nth-except owner (my-addresses))
            request-name (if (zero? (rand-int 2)) name (normalize (str (rand-str 1)
                                                                       "."
                                                                       registrar/root-node)))]

        (registrar/register! {:ens.record/label label} {:from owner})

        (offering-requests/add-request! {:offering-request/name request-name} {:form owner})

        (let [tx-hash (if (= offering-type :buy-now-offering)
                        (buy-now-offering-factory/create-offering! {:offering/name name
                                                                    :offering/price price}
                                                                   {:from owner})
                        (auction-offering-factory/create-offering!
                          {:offering/name name
                           :offering/price price
                           :auction-offering/end-time (to-epoch (t/plus (t/now) (t/weeks 2)))
                           :auction-offering/extension-duration (rand-int 10000)
                           :auction-offering/min-bid-increase (web3/to-wei 0.1 :ether)}
                          {:from owner}))]

          (let [{{:keys [:offering]} :args} (offering-registry/on-offering-added-in-tx tx-hash {:node node
                                                                                                :owner owner})]
            (registrar/transfer! {:ens.record/label label :ens.record/owner offering}
                                 {:from owner})

            (when (and (= offering-type :auction-offering)
                       (zero? (rand-int 2)))
              (auction-offering/bid! {:offering/address offering} {:value price :from buyer}))

            #_(when (zero? (rand-int 2))
                (if (= offering-type :buy-now-offering)
                  (buy-now-offering/buy! {:offering/address offering} {:value price :from buyer})
                  (auction-offering/bid! {:offering/address offering} {:value price :from buyer})))))))))