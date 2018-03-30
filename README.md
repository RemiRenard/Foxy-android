# Foxy

<p align="center"><img height="300" width="300" src="https://ibb.co/bNZVn7" /></p>

How the architecture works ?

<p align="center"><img src="https://preview.ibb.co/fBWmLQ/remirenard_clean_architecture.jpg" /></p>

Explanations of the logic to merge data between database & network :

First, create an observable to get data from the database. (We use SqlBrite in this example) :

```kotlin
/**
 * Get users from the database.
 * @return an observable of a list of users.
 */
private fun getNotificationsFromDb(): Observable<List<Notification>> {
    return Data.database!!.createQuery(TableNotification.DATABASE_TABLE_NAME,
            TableNotification.getNotifications()).mapToList { cursor ->
        val notification = Notification()
        notification.id = cursor.getString(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_ID))
        notification.message = cursor.getString(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_MESSAGE))
        notification.userSource = User(cursor.getString(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_USERNAME)))
        notification.createdAt = Date(cursor.getString(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_CREATED_AT)).toLong())
        notification.type = cursor.getString(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_TYPE))
        notification.song = cursor.getString(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_SONG))
        notification.isRead = cursor.getInt(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_IS_READ)) == 1
        mNotifications.add(notification)
        notification
    }
}
```



Afterthat, create an observable to fetch data from the network. (We use Retrofit in this example) :

```kotlin
/**
 * Fetch notification from the network.
 * @return an observable of a list of notifications.
 */
private fun getNotificationsFromNetwork(token: String): Observable<List<Notification>> {
    return Data.networkService!!
            .getNotifications(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                addNotificationsToDb(it)
            }
            .onErrorReturn {
                EventBus.getDefault().post(NetworkErrorEvent(it))
                mNotifications
            }
}
```

Merge observables :

```kotlin
/**
 * Get the list of notification from the database first and when notifications from the network are fetched,
 * the list of notification is up to date.
 * @return an observable of a list of notifications.
 */
override fun getNotifications(forceNetworkRefresh: Boolean): Observable<List<Notification>> {
    mNotifications.clear()
    return if (Cache.notifications.isNotEmpty() && !forceNetworkRefresh) {
        Observable.just(Cache.notifications)
    } else {
        getNotificationsFromNetwork(Cache.token!!)
                .publish { network -> Observable.merge(network, getNotificationsFromDb().takeUntil(network)) }
                .doOnNext { Cache.notifications = it }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
```
