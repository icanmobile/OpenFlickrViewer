package dependencies

object UITest {
    const val test_core = "androidx.test:core-ktx:${Versions.test_core}"
    const val test_runner = "androidx.test:runner:${Versions.test_runner}"
    const val test_rules = "androidx.test:rules:${Versions.test_runner}"
    const val test_ext = "androidx.test.ext:junit-ktx:${Versions.test_ext}"

    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val espresso_contrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val espresso_idling_resource = "androidx.test.espresso:espresso-idling-resource:${Versions.espresso_idling_resource}"

    const val orchestrator = "androidx.test:orchestrator:${Versions.orchestrator}"

    const val kotlin_test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    const val mockK = "io.mockk:mockk-android:${Versions.mockk}"
}