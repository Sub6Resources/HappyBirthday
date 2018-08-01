package com.sub6resources.happybirthday

import android.graphics.Color
import com.sub6resources.utilities.BaseSettingsActivity
import com.sub6resources.utilities.group
import com.sub6resources.utilities.setting
import com.sub6resources.utilities.settingsActivity

class SettingsActivity: BaseSettingsActivity() {
    override val settings = settingsActivity {
        title = "Settings"
        group("Message Settings") {
            setting("hb_splitby", "word") {
                description = "Split messages by"
                options = arrayOf("word", "letter")
            }
            setting("hb_delay", 5000L) {
                description = "Millisecond Delay Between Messages"
                units = "ms"
            }
        }
    }
}