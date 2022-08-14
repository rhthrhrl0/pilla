package com.example.ssu_contest_eighteen_pomise.dto

data class PillDetailInfoResponse(
    val header: String,
    val body: String
)

data class Header(
    val resultCode: String,
    val resultMsg: String
)

data class Body(
    val items: Any,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

data class Item(
    val ATC_CODE: String,
    val BAR_CODE: String,
    val CANCEL_DATE: Any,
    val CANCEL_NAME: String,
    val CHANGE_DATE: String,
    val CHART: String,
    val CNSGN_MANUF: Any,
    val DOC_TEXT: String,
    val EDI_CODE: String,
    val EE_DOC_DATA: String,
    val EE_DOC_ID: String,
    val ENTP_NAME: String,
    val ENTP_NO: String,
    val ETC_OTC_CODE: String,
    val GBN_NAME: String,
    val INDUTY_TYPE: String,
    val INGR_NAME: String,
    val INSERT_FILE: String,
    val ITEM_NAME: String,
    val ITEM_PERMIT_DATE: String,
    val ITEM_SEQ: String,
    val MAIN_ITEM_INGR: String,
    val MAKE_MATERIAL_FLAG: String,
    val MATERIAL_NAME: String,
    val NARCOTIC_KIND_CODE: Any,
    val NB_DOC_DATA: String,
    val NB_DOC_ID: String,
    val NEWDRUG_CLASS_NAME: String,
    val PACK_UNIT: String,
    val PERMIT_KIND_NAME: String,
    val PN_DOC_DATA: Any,
    val REEXAM_DATE: Any,
    val REEXAM_TARGET: Any,
    val STORAGE_METHOD: String,
    val TOTAL_CONTENT: String,
    val UD_DOC_DATA: String,
    val UD_DOC_ID: String,
    val VALID_TERM: String
)