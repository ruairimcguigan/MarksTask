package com.demo.movies.testdata

object TestData {

    // Inputs
    const val MOCKED_RESPONSE = "movies_response.json"
    const val PORT = 8080
    const val MOCKED_NO_RESPONSE_AMOUNT = 0
    const val MOCKED_NO_RESPONSE_AUTH_CODE = "null"
    const val OKHTTP = "okhttp"
    const val MOVIE_PARCEL = "MOVIE"

    // Success
    const val CREATED = 201

    // Error codes
    const val MOVED = 301
    const val BAD_REQUEST = 400
    const val UNAUTHORISED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val INTERNAL_ERROR = 500
    const val BAD_GATEWAY = 502
}