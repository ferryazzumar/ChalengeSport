package com.example.chalengesport.commons.utils.other

import com.example.chalengesport.R

class Constants {
    companion object{
        // BASE API
        const val BASE_URL = "https://www.thesportsdb.com/api/"
//        const val BASE_URL_STORE = "http://192.168.100.100:8000/api/"

        const val detailTeamURL = "v1/json/1/lookupteam.php"
        const val listTeamURL = "v1/json/1/search_all_teams.php"

        // Product URL
        const val productURL = "v1/products"
        const val productV2URL = "v2/products"
        const val showProductURL = "v2/product/{param}"
        const val showProductHomeURL = "v2/product/{param}"
        const val getBrandURL = "v1/product-brands"
        const val productCategoriesURL = "v1/product-categories"
        const val productCategoriesByParentURL = "v1/product-categories/{parent}/children"
        const val productCategoryURL = "v1/product-category/{param}"
        const val productDiscountURL = "v1/product-discounts"
        const val productVariantsURL = "v1/product-variants"
        const val productVariantsByParamURL = "v1/product-variant/{param}"
        const val productVariantValuesURL = "v1/product-variant-values"
        const val productVariantValuesByVariantURL = "v1/product-variant-values/byvariant/{variant}"
        const val productShowVariantValuesURL = "v1/product-variant-value/{param}"
        const val productAllVoucherURL = "v1/product-vouchers"
        const val productVoucherURL = "v1/product-vouchers/available/by-products"
        const val productFlashSaleURL = "v2/flash-sales"
        const val productFlashSalesBannerURL = "v2/flash-sales"
        const val productFlashSalesBySessionURL = "v2/flash-sale-products/bysession/{session}"

        // Address URl
        const val addressUrl = "v1/my-address"
        const val addressListURL = "v1/my-addresses"
        const val provinceURL = "v1/region/province"
        const val cityURL = "v1/region/city"
        const val subDistrictURL = "v1/region/sub-district"

        // Auth URL
        const val loginURL = "v1/login"
        const val registerURL = "v1/register"
        const val profileURL = "v1/user"

        const val databaseName = "panintistore.db"
    }
}

object ConstantObject {

    object API {
        const val HeaderKey = "Bearer"
    }

    object Shared {
        const val tokenFileName = "store_token"
        const val accessTokenFileName = "access_token"
        const val oauthFileName = "oauth"
        const val tokenKey = "key_token_20"
        const val accessTokenKey = "key_access_token_20"
        const val oAuthKey = "key_oauth_20"
    }

    object Login {
        const val loginAccess = "loginLock"
        const val loginKey = "isOpenKey"
    }

    object Paging {
        const val beginPage = 1
        const val requestSizeAll = 0
        const val requestSizeSmall = 5
        const val requestSizeNormal = 10
        const val requestSizeDouble = 20
        const val requestSizeHuge = 30
    }

    object Sort {
        const val ascending = "asc"
        const val descending = "desc"
    }

    object Facebook {
        const val field = "field"
        const val email = "email"
        const val publicProfile = "public_profile"
        const val firstName = "first_name"
        const val middleName = "middle_name"
        const val lastName = "last_name"
        const val name = "name"
        const val nameFormat = "name_format"
        const val id = "id"
        const val picture = "picture.width(300).height(300)"
    }

    object Search {
        const val shortDelay = 150L
        const val delay = 300L
        const val longDelay = 500L
    }

    object Slider {
        const val period = 1750L
        const val delay = 250L
    }

    object ImageExtension {
        const val JPG = "JPEG"
        const val PNG = "PNG"
        const val GIF = "GIF"
        const val BITMAP = "BMP"
    }

    object File {
        object Location{
            const val basePath = "Paninti/"
            const val storePath = "Store/"
        }

        object MimeType{
            const val image = "image/jpeg"
            const val pdf = "application/pdf"
        }

        object Image {
            const val defaultFileName = "Panstor-Image"
        }

        object Pending {
            const val isPending = 1
            const val notPending = 0
        }
    }
}

val baseFragmentDestination = setOf(
    R.id.main_navigation
)