package com.chat.youknow.data.response

data class MessageHistoryResponse(
    var pageable: Pageable = Pageable(),
    var messageInfo: MutableList<MessageInfo> = mutableListOf(),
    var roomInfo: RoomInfo = RoomInfo()
) {
    data class RoomInfo(
        var isGroup: Boolean = false,
        var unreadCount: Int = 0,
        var isClearMessage: Boolean = false,
        var clearMessageTime: String = "",
        var p2p: P2P = P2P()
    )
    data class P2P(
        var userId: String = "",
        var username: String = "",
        var url: String = "",
        var isMuted: Boolean = false,
        var friendStatus: Int = 0
    )
    data class Pageable(
        var prev: String? = null,
        var next: String? = null
    )
    data class MessageInfo(
        var timeMillisecond: Long?=null,
        var mainContentType: Int? = 0,
        var deleteStatus: String ?= "",
        var roomId: String ?= "",
        var mediaData: MediaData = MediaData()
    )
    data class MediaData(
        var sender: Sender = Sender(),
        var content: Content = Content()
    )
    data class Sender(
        var id: String = "",
        var username: String = "",
        var url: String = ""
    )
    data class Content(
        var subContentType: Int = 0,
        var messageId: String = "",
        var uuid: String = "",
        var data: Data = Data()
    )
    data class Data(
        var text: String =""
    )
}

data class SendMessageResponse(
    var roomInfo: MessageHistoryResponse.RoomInfo = MessageHistoryResponse.RoomInfo(),
    var messageInfo: MutableList<MessageHistoryResponse.MessageInfo> = mutableListOf()
)