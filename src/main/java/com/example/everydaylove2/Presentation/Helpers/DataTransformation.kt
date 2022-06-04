package com.example.everydaylove2.Presentation.Helpers

fun DataTransformation(month: String, day: String): String {
    var TransformedData: String = ""
    when (month) {
        "1" -> TransformedData = day + " Января"
        "2" -> TransformedData = day + " Февраля"
        "3" -> TransformedData = day + " Марта"
        "4" -> TransformedData = day + " Апреля"
        "5" -> TransformedData = day + " Мая"
        "6" -> TransformedData = day + " Июня"
        "7" -> TransformedData = day + " Июля"
        "8" -> TransformedData = day + " Августа"
        "9" -> TransformedData = day + " Сентября"
        "10" -> TransformedData = day + " Октября"
        "11" -> TransformedData = day + " Ноября"
        "12" -> TransformedData = day + " Декабря"
    }

    return TransformedData
}
