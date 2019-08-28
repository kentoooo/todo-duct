(ns todo.handler.todos
  (:require [ataraxy.response :as response]
            [integrant.core :as ig]
            [todo.boundary.db.todos :as todos]))

(defmethod ig/init-key ::list [_ {:keys [db]}]
  (fn [_]
    (let [todos (todos/get-todos db)]
      [::response/ok todos])))

(defmethod ig/init-key ::fetch [_ {:keys [db]}]
  (fn [{[_ id] :ataraxy/result}]
    (let [todo (todos/fetch-todo db id)]
      [::response/ok todo])))

(defmethod ig/init-key ::create [_ {:keys [db]}]
  (fn [{[_ params] :ataraxy/result}]
    (let [result (todos/create-todo db params)
          id (:id (first result))]
      [::response/created (str "/todos/" id)])))

(defmethod ig/init-key ::update [_ {:keys [db]}]
  (fn [{[_ id params] :ataraxy/result}]
    (todos/update-todo db id params)
    [::response/no-content]))

(defmethod ig/init-key ::delete [_ {:keys [db]}]
  (fn [{[_ id] :ataraxy/result}]
    (todos/delete-todo db id)
    [::response/no-content]))
