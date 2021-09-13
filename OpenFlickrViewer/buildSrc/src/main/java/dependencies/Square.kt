package dependencies

object Square{
  const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
  const val retrofit_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
  const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
  const val okHttp_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
}