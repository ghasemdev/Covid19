package com.jakode.covid19.utils

import java.util.*

object CountryRename {
    private val englishToPersianMap = mapOf(
        "Afghanistan" to "افغانستان", "Albania" to "آلبانی", "Algeria" to "الجزیره",
        "Andorra" to "آندورا", "Angola" to "آنگولا", "Aruba" to "آروبا",
        "Anguilla" to "آنگویلا", "Antigua-and-Barbuda" to "آنتیگوا و باربودا",
        "Argentina" to "آرژانتین", "Armenia" to "ارمنستان", "Australia" to "استرالیا",
        "Austria" to "اتریش", "Azerbaijan" to "آذربایجان", "Bahamas" to "باهاما",
        "Bahrain" to "بحرین", "Bangladesh" to "بنگلادش", "Barbados" to "باربادوس",
        "Belarus" to "بلاروس", "Belgium" to "بلژیک", "Belize" to "بلیز",
        "Benin" to "بنین", "Bermuda" to "برمودا", "Bhutan" to "بوتان", "Bolivia" to "بولیوی",
        "Bosnia-and-Herzegovina" to "بوسنی – هرزگوین", "Botswana" to "بوتساوانا",
        "Brazil" to "برزیل", "British-Virgin-Islands" to "جزایر ویرجین بریتانیا",
        "Britain" to "بریتانیا", "Brunei" to "بروئنی", "Bulgaria" to "بلغارستان",
        "Burkina-Faso" to "بورکینا فاسو", "Burma" to "میانمار", "Burundi" to "بوروندی",
        "CAR" to "جمهوری آفریقای مرکزی", "Cabo-Verde" to "کیپ ورد", "Cambodia" to "کامبوج",
        "Cameroon" to "کامرون", "Canada" to "کانادا", "Caribbean-Netherlands" to "کارائیب هلند",
        "Cayman-Islands" to "جزایر کایمن", "Cape Verde Islands" to "کیپ ورد", "Chad" to "چاد",
        "Channel-Islands" to "جزیره\u200Cهای مانش", "Chile" to "شیلی", "China" to "چین",
        "Colombia" to "کلمبیا", "Comoros" to "مجمع\u200Cالجزایر قمر", "Congo" to "کنگو",
        "Costa-Rica" to "کاستاریکا", "Croatia" to "کرواسی", "Cuba" to "کوبا",
        "Cyprus" to "قبرس", "Czechia" to "جمهوری چک", "DRC" to "جمهوری دموکراتیک کنگو",
        "Denmark" to "دانمارک", "Djibouti" to "جیبوتی", "Dominica" to "دومینیکا",
        "Dominican-Republic" to "جمهوری دومینیک", "Ecuador" to "اکوادور", "Egypt" to "مصر",
        "El-Salvador" to "السالوادور", "Equatorial-Guinea" to "گینه استوایی",
        "Eritrea" to "اریتره", "Estonia" to "استونی", "Eswatini" to "اسواتینی",
        "Ethiopia" to "اتیوپی", "Faeroe-Islands" to "جزایر فارو",
        "Falkland-Islands" to "جزایر فالکلند", "Fiji" to "فیجی", "Finland" to "فنلاند",
        "France" to "فرانسه", "French-Guiana" to "گویان فرانسه",
        "French-Polynesia" to "پلی\u200Cنزی فرانسه", "Gabon" to "گابن",
        "Gambia" to "گامبیا", "Georgia" to "گرجستان", "Germany" to "آلمان",
        "Ghana" to "غنا", "Gibraltar" to "جبل طارق", "Greece" to "یونان",
        "Greenland" to "گرینلند", "Grenada" to "گرانادا", "Guadeloupe" to "جزیرهٔ گوادلوپ",
        "Guatemala" to "گواتمالا", "Guinea" to "گینه", "Guinea-Bissau" to "گینه بیسائو",
        "Guyana" to "گویان", "Haiti" to "هائیتی", "Honduras" to "هندوراس",
        "Hong-Kong" to "هنگ کنگ", "Hungary" to "مجارستان", "Iceland" to "ایسلند",
        "India" to "هند", "Indonesia" to "اندونزی", "Iran" to "ایران",
        "Iraq" to "عراق", "Ireland" to "ایرلند", "Isle-of-Man" to "جزیرۀ من",
        "Israel" to "اسرائيل", "Italy" to "ایتالیا", "Ivory-Coast" to "ساحل عاج",
        "Jamaica" to "جامائیکا", "Japan" to "ژاپن", "Jordan" to "اردن",
        "Kazakhstan" to "قزاقستان", "Kenya" to "کنیا", "Kuwait" to "کویت",
        "Kyrgyzstan" to "قرقیزستان", "Laos" to "لائوس", "Latvia" to "لتونی",
        "Lebanon" to "لبنان", "Lesotho" to "لسوتو", "Liberia" to "لیبریا",
        "Libya" to "لیبی", "Liechtenstein" to "لیختن\u200Cاشتاین", "Lithuania" to "لیتوانی",
        "Luxembourg" to "لوکزامبورگ", "Macao" to "ماکائو", "Macedonia" to "مقدونیه",
        "Madagascar" to "ماداگاسکار", "Malawi" to "مالاوی", "Malaysia" to "مالزی",
        "Maldives" to "مالدیو", "Mali" to "مالی", "Malta" to "مالتا",
        "Marshall-Islands" to "جزایر مارشال", "Martinique" to "مارتینیک",
        "Mauritania" to "موریتانی", "Mauritius" to "موریس", "Mayotte" to "مایوت",
        "Mexico" to "مکزیک", "Moldova" to "مولداوری", "Monaco" to "موناکو",
        "Mongolia" to "مغولستان", "Montenegro" to "مونتنگرو", "Montserrat" to "مونتسرات",
        "Morocco" to "مراکش", "Mozambique" to "موزامبیک", "Myanmar" to "میانمار",
        "Namibia" to "نامبیا", "Nepal" to "نپال", "Netherlands" to "هلند",
        "New-Caledonia" to "کالدونیای جدید", "New-Zealand" to "نیوزیلند",
        "Nicaragua" to "نیکاراگوئه", "Niger" to "نیجر", "North-Macedonia" to "مقدونیه شمالی",
        "Nigeria" to "نیجریه", "North Korea" to "کره شمالی", "Norway" to "نروژ",
        "Oman" to "عمان", "Pakistan" to "پاکستان", "Palestine" to "فلسطین",
        "Panama" to "پاناما", "Papua-New-Guinea" to "پاپوا گینه نو", "Paraguay" to "پاراگوئه",
        "Peru" to "پرو", "Philippines" to "فیلیپین", "Poland" to "لهستان",
        "Portugal" to "پرتغال", "Qatar" to "قطر", "Romania" to "رومانی",
        "Russia" to "روسیه", "Rwanda" to "روآندا", "S-Korea" to "کره جنوبی",
        "Saint-Kitts-and-Nevis" to "سنت کیتس و نویس", "Saint-Lucia" to "سنت لوسیا",
        "Saint-Martin" to "سن مارتن", "Saint-Pierre-Miquelon" to "سن پیر و میکلون",
        "San-Marino" to "سن مارینو", "Sao-Tome-and-Principe" to "سائوتومه و پرنسیپ",
        "Saudi-Arabia" to "عربستان سعودی", "Scotland" to "اسکاتلند", "Senegal" to "سنگال",
        "Serbia" to "صربستان", "Seychelles" to "جمهوری سیشل", "Sierra-Leone" to "سیرا لئون",
        "Singapore" to "سنگاپور", "Sint-Maarten" to "سنت مارتین", "Slovakia" to "اسلواکی",
        "Slovenia" to "اسلونی", "Solomon-Islands" to "جزایر سلیمان", "Somalia" to "سومالی",
        "South-Africa" to "آفریقای جنوبی", "South-Sudan" to "سودان جنوبی", "Spain" to "اسپانیا",
        "Sri-Lanka" to "سریلانکا", "St-Barth" to "سن بارتلمی",
        "St-Vincent-Grenadines" to "سنت وینسنت و گرنادین\u200Cها", "Sudan" to "سودان",
        "Suriname" to "سورینام", "Swaziland" to "سوارزیلند", "Sweden" to "سوئد",
        "Switzerland" to "سوئیس", "Syria" to "سوریه", "Taiwan" to "تایوان",
        "Tajikistan" to "تاجیکستان", "Tanzania" to "تانزانیا", "Thailand" to "تایلند",
        "Timor-Leste" to "تیمور شرقی", "Togo" to "توگو",
        "Trinidad-and-Tobago" to "ترینیداد و توباگو", "Tunisia" to "تونس", "Turkey" to "ترکیه",
        "Turks-and-Caicos" to "جزایر تورکس و کایکوس", "Turkmenistan" to "ترکمنستان",
        "Tuvalu" to "توالا", "Uganda" to "اوگاندا", "Ukraine" to "اوکراین",
        "UAE" to "امارات متحده عربی", "UK" to "انگلستان", "USA" to "ایالات متحده آمریکا",
        "Uruguay" to "اوروگوئه", "Uzbekistan" to "ازبکستان", "Vanuatu" to "وانوآتو",
        "Vatican-City" to "واتیکان", "Venezuela" to "ونزوئلا", "Vietnam" to "ویتنام",
        "Wallis-and-Futuna" to "والیس و فوتونا", "Western-Sahara" to "صحرای غربی",
        "Wales" to "ولز", "Samoa" to "ساموآ", "Yemen" to "یمن", "Yugoslavia" to "یوگوسلاوی",
        "Zaire" to "زئیر", "Zambia" to "زامبیا", "Zimbabwe" to "زیمباوه"
    )

    fun englishToPersian(word: String): String? {
        return if (isPersian()) englishToPersianMap[word] else word
    }

    fun search(query: String): List<String> {
        val statistics = ArrayList<String>()

        for (value in englishToPersianMap.values) {
            if (value.contains(query)) {
                statistics.add(englishToPersianMap.getKey(value) ?: error(""))
            }
        }
        return statistics
    }
}