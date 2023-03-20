//package com.farasatnovruzov.recyclerlist
//
//import az.turanbank.mlb.domain.user.data.MainRepositoryType
//import az.turanbank.mlb.util.EnumLangType
//import javax.inject.Inject
//
//class ReadNotificationUseCase @Inject constructor(
//    val mainRepositoryType: MainRepositoryType
//) {
//    fun execute(
//        custNotificationTokenId: ArrayList<Long>,
//        compId: Long?,
//        custId: Long,
//        token: String?,
//        lang: EnumLangType
//    ) = mainRepositoryType.readNotification(custNotificationTokenId, compId, custId, token, lang)
//}