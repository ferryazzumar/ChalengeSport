package com.example.chalengesport.service.api

abstract class BaseApiResponse {
    var status : Any? = null
    var message : String? = ""
    var error : Boolean? = false
    var total : Int? = 0
    var totalReq : Int? = 0
    var next_page : Int? = 0

}