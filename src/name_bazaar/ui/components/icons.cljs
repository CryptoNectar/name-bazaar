(ns name-bazaar.ui.components.icons
  (:refer-clojure :exclude [filter])
  (:require [district0x.ui.utils :refer [create-icon]]))

(def bookmark-outline (create-icon "M17,18L12,15.82L7,18V5H17M17,3H7A2,2 0 0,0 5,5V21L12,18L19,21V5C19,3.89 18.1,3 17,3Z"))
(def bookmark-remove (create-icon "M17,3A2,2 0 0,1 19,5V21L12,18L5,21V5C5,3.89 5.9,3 7,3H17M8.17,8.58L10.59,11L8.17,13.41L9.59,14.83L12,12.41L14.41,14.83L15.83,13.41L13.41,11L15.83,8.58L14.41,7.17L12,9.58L9.59,7.17L8.17,8.58Z"))
(def chevron-down (create-icon "M7.41,8.58L12,13.17L16.59,8.58L18,10L12,16L6,10L7.41,8.58Z"))
(def chevron-up (create-icon "M7.41,15.41L12,10.83L16.59,15.41L18,14L12,8L6,14L7.41,15.41Z"))
(def close (create-icon "M19,6.41L17.59,5L12,10.59L6.41,5L5,6.41L10.59,12L5,17.59L6.41,19L12,13.41L17.59,19L19,17.59L13.41,12L19,6.41Z"))
(def filter (create-icon "M3,2H21V2H21V4H20.92L14,10.92V22.91L10,18.91V10.91L3.09,4H3V2Z"))
(def filter-remove (create-icon "M14.76,20.83L17.6,18L14.76,15.17L16.17,13.76L19,16.57L21.83,13.76L23.24,15.17L20.43,18L23.24,20.83L21.83,22.24L19,19.4L16.17,22.24L14.76,20.83M2,2H20V2H20V4H19.92L13,10.92V22.91L9,18.91V10.91L2.09,4H2V2Z"))
(def plus (create-icon "M19,13H13V19H11V13H5V11H11V5H13V11H19V13Z"))


