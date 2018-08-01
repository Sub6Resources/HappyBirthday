package com.sub6resources.happybirthday

import android.Manifest
import android.content.Intent
import android.support.design.widget.Snackbar
import android.telephony.SmsManager
import android.text.InputType
import android.view.MenuItem
import android.widget.Toast
import com.sub6resources.utilities.BaseActivity
import com.sub6resources.utilities.dialog
import com.sub6resources.utilities.onClick
import com.sub6resources.utilities.sharedPreferences
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity(R.layout.activity_main) {

    override val toolbar = R.id.toolbar_main
    override val menu = R.menu.main

    override fun setUp() {
        checkListOfPermissions(
                permissions = listOf(Manifest.permission.SEND_SMS),
                onGranted = {

                },
                onDenied = {
                    finish()
                },
                showExplanation = { _, code ->
                    recheckPermission(code)
                }
        )

        if (Intent.ACTION_SEND == intent.action) {
            if ("text/plain" == intent.type) {
                startSending(intent.getStringExtra(Intent.EXTRA_TEXT))
            }
        }

        btn_sendmessage.onClick {
            startSending(et_input.text.toString())
            et_input.setText("")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startSending(message: String) {
        dialog {
            title("Enter Number")
            inputType(InputType.TYPE_CLASS_PHONE)
            input("+1", "") { _, input ->
                val newInput = if(input.length == 10) {
                    "+1$input"
                } else {
                    input
                }
                sendSequence(newInput.toString(), message)
            }
            val splitBy = sharedPreferences.getString("hb_splitby", "word")
            positiveText("Send message $splitBy by $splitBy")
        }.show()
    }

    private fun sendSequence(number: String, text: String) {
        launch {
            val messages = when(sharedPreferences.getString("hb_splitby", "word")) {
                "letter" -> text.toList().map { it.toString() }
                "word" -> text.split(" ")
                else -> text.split(" ")
            }
            messages.forEach {
                sendSms(number, it)
                delay(sharedPreferences.getLong("hb_delay", 5000L), TimeUnit.MILLISECONDS)
            }
        }
        Snackbar.make(container, "Message Sent!", Snackbar.LENGTH_LONG).show()
    }

    private fun sendSms(number: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(number, null, message, null, null)
        } catch (ex: Exception) {
            Toast.makeText(applicationContext, ex.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

}
