package com.farasatnovruzov.recyclerlist


data class ItemModel(
    val id: Long,
    val notificationId: Long,
    val title: String,
    val content: String,
    val image: String?,
    val date: String,
    val notificationTypeId: Int,
    val notificationType: String,
    var isRead: Int,
    var isSelected: Boolean = false,
    var loanOrderId: Long?,
    var page: Int?
){

    override fun equals(other: Any?): Boolean {
//        return super.equals(other)
        if(javaClass != other?.javaClass){
            return false
        }

        other as ItemModel

        if(id != other.id){
            return false
        }
        if(notificationId != other.notificationId){
            return false
        }
//        if(title != other.title){
//            return false
//        }
        if(content != other.content){
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}