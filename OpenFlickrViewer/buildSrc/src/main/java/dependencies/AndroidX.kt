package dependencies

object AndroidX {
  const val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
  const val app_compat = "androidx.appcompat:appcompat:${Versions.app_compat}"
  const val constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"

  const val fragment_testing = "androidx.fragment:fragment-testing:${Versions.fragment}"
  const val fragment_ktx = "androidx.fragment:fragment-ktx:${Versions.fragment}"

  const val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime:${Versions.lifecycle}"
  const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"

  const val nav_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${Versions.nav_component}"
  const val nav_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.nav_component}"
  const val nav_runtime = "androidx.navigation:navigation-runtime:${Versions.nav_component}"
  const val nav_testing = "androidx.navigation:navigation-testing:${Versions.nav_component}"

  const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
  const val room_ktx = "androidx.room:room-ktx:${Versions.room}"
  const val room_compiler = "androidx.room:room-compiler:${Versions.room}"
}