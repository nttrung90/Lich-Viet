package com.lichviet.vannien.data

/**
 * Kho tàng Ca dao, Tục ngữ, Thành ngữ & Danh ngôn Việt Nam
 */
object FolkQuoteRepository {

    data class FolkQuote(
        val quote: String,
        val author: String = "Thành ngữ Việt Nam"
    )

    private val quotes = listOf(
        FolkQuote("Chín bỏ làm mười", "Thành ngữ Việt Nam"),
        FolkQuote("Ăn quả nhớ kẻ trồng cây", "Tục ngữ Việt Nam"),
        FolkQuote("Uống nước nhớ nguồn", "Tục ngữ Việt Nam"),
        FolkQuote("Thuận vợ thuận chồng, tát biển Đông cũng cạn", "Tục ngữ Việt Nam"),
        FolkQuote("Một cây làm chẳng nên non\nBa cây chụm lại nên hòn núi cao", "Ca dao Việt Nam"),
        FolkQuote("Bầu ơi thương lấy bí cùng\nTuy rằng khác giống nhưng chung một giàn", "Ca dao Việt Nam"),
        FolkQuote("Nhiễu điều phủ lấy giá gương\nNgười trong một nước phải thương nhau cùng", "Ca dao Việt Nam"),
        FolkQuote("Công cha như núi Thái Sơn\nNghĩa mẹ như nước trong nguồn chảy ra", "Ca dao Việt Nam"),
        FolkQuote("Lời nói chẳng mất tiền mua\nLựa lời mà nói cho vừa lòng nhau", "Tục ngữ Việt Nam"),
        FolkQuote("Có công mài sắt, có ngày nên kim", "Tục ngữ Việt Nam"),
        FolkQuote("Đi một ngày đàng, học một sàng khôn", "Tục ngữ Việt Nam"),
        FolkQuote("Lá lành đùm lá rách", "Tục ngữ Việt Nam"),
        FolkQuote("Gần mực thì đen, gần đèn thì rạng", "Tục ngữ Việt Nam"),
        FolkQuote("Học ăn, học nói, học gói, học mở", "Tục ngữ Việt Nam"),
        FolkQuote("Đói cho sạch, rách cho thơm", "Tục ngữ Việt Nam"),
        FolkQuote("Thương người như thể thương thân", "Tục ngữ Việt Nam"),
        FolkQuote("Tấc đất tấc vàng", "Tục ngữ Việt Nam"),
        FolkQuote("Ở hiền gặp lành", "Tục ngữ Việt Nam"),
        FolkQuote("Trăm năm tích đức tu thân\nMột ngày đắc đạo hóa thần thành tiên", "Thành ngữ dân gian"),
        FolkQuote("Kim vàng ai nỡ uốn câu\nNgười khôn ai nỡ nói nhau nặng lời", "Ca dao Việt Nam")
    )

    fun getDailyQuote(dayOfYear: Int): FolkQuote {
        val index = (dayOfYear % quotes.size + quotes.size) % quotes.size
        return quotes[index]
    }
}
