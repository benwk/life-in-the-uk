package uk.robin.app.data

import android.content.Context

/** How bilingual text is shown (docs/product/07-language-experience.md). */
enum class LensMode { NATIVE_FIRST, SIDE_BY_SIDE, ENGLISH_FIRST }

/**
 * Local-only learner data. SharedPreferences is enough for the skeleton;
 * move to Room once FSRS review history is stored per fact.
 */
class LearnerStore(context: Context) {
    private val prefs = context.getSharedPreferences("learner", Context.MODE_PRIVATE)

    var learnerLocale: String
        get() = prefs.getString("learnerLocale", "zh-Hans")!!
        set(value) = prefs.edit().putString("learnerLocale", value).apply()

    var lensMode: LensMode
        get() = LensMode.valueOf(prefs.getString("lensMode", LensMode.SIDE_BY_SIDE.name)!!)
        set(value) = prefs.edit().putString("lensMode", value.name).apply()

    var stamps: Set<String>
        get() = prefs.getStringSet("stamps", emptySet())!!.toSet()
        set(value) = prefs.edit().putStringSet("stamps", value).apply()

    var mistakes: Set<String>
        get() = prefs.getStringSet("mistakes", emptySet())!!.toSet()
        set(value) = prefs.edit().putStringSet("mistakes", value).apply()

    var seen: Set<String>
        get() = prefs.getStringSet("seen", emptySet())!!.toSet()
        set(value) = prefs.edit().putStringSet("seen", value).apply()
}
