//package com.farasatnovruzov.recyclerlist
//
//import android.content.SharedPreferences
//import az.turanbank.mlb.data.remote.model.response.NotificationResult
//import az.turanbank.mlb.presentation.base.BaseViewModel
//import az.turanbank.mlb.presentation.base.BaseViewModelInputs
//import az.turanbank.mlb.presentation.base.BaseViewModelOutputs
//import az.turanbank.mlb.util.EnumLangType
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.rxkotlin.addTo
//import io.reactivex.schedulers.Schedulers
//import io.reactivex.subjects.PublishSubject
//import javax.inject.Inject
//
//
//interface NotificationViewModelInputs : BaseViewModelInputs {
//    fun getNotificationList()
//    fun deleteNotification(list: ArrayList<Long>)
//    fun readNotification(list: ArrayList<Long>)
//}
//
//interface NotificationViewModelOutputs : BaseViewModelOutputs {
//    fun notificationListSuccess(): PublishSubject<ArrayList<NotificationResult>>
//    fun notificationDeleteSuccess(): PublishSubject<Boolean>
//    fun notificationReadSuccess(): PublishSubject<Boolean>
//}
//
//class NotificationViewModel @Inject constructor(
//    private val getIndividualNotificationListUseCase: GetIndividualNotificationListUseCase,
//    private val getJuridicalNotificationListUseCase: GetJuridicalNotificationListUseCase,
//    private val deleteNotificationUseCase: DeleteNotificationUseCase,
//    private val readNotificationUseCase: ReadNotificationUseCase,
//    sharedPrefs: SharedPreferences
//) : BaseViewModel(), NotificationViewModelInputs, NotificationViewModelOutputs {
//    override val inputs: NotificationViewModelInputs = this
//    override val outputs: NotificationViewModelOutputs = this
//    val isCustomerJuridical = sharedPrefs.getBoolean("isCustomerJuridical", false)
//    val isCustomerType =  sharedPrefs.getInt("bankCustomerType", 0)
//    val token = sharedPrefs.getString("token", "")
//    val custId = sharedPrefs.getLong("custId", 0L)
//    val compId = sharedPrefs.getLong("compId", 0L)
//    val lang = sharedPrefs.getString("lang", "az")
//    var enumLangType = EnumLangType.AZ
//
//    private val notificationList = PublishSubject.create<ArrayList<NotificationResult>>()
//    private val deleteNotificationResult = PublishSubject.create<Boolean>()
//    private val readNotificationResult = PublishSubject.create<Boolean>()
//
//    override fun getNotificationList() {
//        when (lang) {
//            "az" -> enumLangType = EnumLangType.AZ
//            "ru" -> enumLangType = EnumLangType.RU
//            "en" -> enumLangType = EnumLangType.EN
//        }
//
//        if (isCustomerJuridical) {
//            getJuridicalNotificationListUseCase.execute(compId, custId, token, enumLangType)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    if (it.status.statusCode == 1) {
//                        notificationList.onNext(it.notificationList)
//                    } else {
//                        error.onNext(it.status.statusCode)
//                    }
//                }, {
//                    error.onNext(1878)
//                    it.printStackTrace()
//                }, {
//
//                })
//                .addTo(subscriptions)
//        } else {
//            getIndividualNotificationListUseCase.execute(custId, token, enumLangType)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    if (it.status.statusCode == 1) {
//
//                        notificationList.onNext(it.notificationList)
//                    } else {
//                        error.onNext(it.status.statusCode)
//                    }
//                }, {
//                    error.onNext(1878)
//                    it.printStackTrace()
//                }, {
//
//                })
//                .addTo(subscriptions)
//
//        }
//    }
//
//    override fun deleteNotification(list: ArrayList<Long>) {
//        when (lang) {
//            "az" -> enumLangType = EnumLangType.AZ
//            "ru" -> enumLangType = EnumLangType.RU
//            "en" -> enumLangType = EnumLangType.EN
//        }
//
//        if (isCustomerJuridical) {
//            deleteNotificationUseCase.execute(list, compId, custId, token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    if (it.status.statusCode == 1) {
//
//                        deleteNotificationResult.onNext(true)
//                    } else {
//                        error.onNext(it.status.statusCode)
//                    }
//                }, {
//                    error.onNext(1878)
//                    it.printStackTrace()
//                }, {
//
//                })
//                .addTo(subscriptions)
//        } else {
//            deleteNotificationUseCase.execute(list, null, custId, token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    if (it.status.statusCode == 1) {
//                        deleteNotificationResult.onNext(true)
//                    } else {
//                        error.onNext(it.status.statusCode)
//                    }
//                }, {
//                    error.onNext(1878)
//                    it.printStackTrace()
//                }, {
//
//                })
//                .addTo(subscriptions)
//
//        }
//    }
//
//    override fun readNotification(list: ArrayList<Long>) {
//        when (lang) {
//            "az" -> enumLangType = EnumLangType.AZ
//            "ru" -> enumLangType = EnumLangType.RU
//            "en" -> enumLangType = EnumLangType.EN
//        }
//
//        if (isCustomerJuridical) {
//            readNotificationUseCase.execute(list, compId, custId, token, enumLangType)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    if (it.status.statusCode == 1) {
//
//                        readNotificationResult.onNext(true)
//                    } else {
//                        error.onNext(it.status.statusCode)
//                    }
//                }, {
//                    error.onNext(1878)
//                    it.printStackTrace()
//                }, {
//
//                })
//                .addTo(subscriptions)
//        } else {
//            readNotificationUseCase.execute(list, null, custId, token, enumLangType)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    if (it.status.statusCode == 1) {
//                        readNotificationResult.onNext(true)
//                    } else {
//                        error.onNext(it.status.statusCode)
//                    }
//                }, {
//                    error.onNext(1878)
//                    it.printStackTrace()
//                }, {
//
//                })
//                .addTo(subscriptions)
//
//        }
//    }
//
//    override fun notificationListSuccess() = notificationList
//    override fun notificationDeleteSuccess() = deleteNotificationResult
//    override fun notificationReadSuccess() = readNotificationResult
//}