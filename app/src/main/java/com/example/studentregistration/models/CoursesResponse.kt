package com.example.studentregistration.models


data class CoursesResponse(
    var course_id: String,
    var course_code:String,
    var course_name:String,
    var description:String,
    var instructor:String
)
