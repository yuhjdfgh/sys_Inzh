package com.example.finalappp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ChoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice)
        showChoiceDialog()
    }

    private fun showChoiceDialog() {
        AlertDialog.Builder(this)
            .setTitle("Выбор")
            .setMessage("Что вы хотите увидеть?")
            .setCancelable(false)
            .setPositiveButton("Художники") { _, _ ->
                navigateToNextActivity("painters")
            }
            .setNegativeButton("Картины") { _, _ ->
                navigateToNextActivity("pictures")
            }
            .create()
            .show()
    }

    private fun navigateToNextActivity(choice: String) {
        val intent = Intent(this, threeActIzbrLikeDiz::class.java)
        //intent.putExtra("choice", choice)

        val sharedPrefs = getSharedPreferences("CurrentChoicePicPaint", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString("choiceKeyPicPaint", choice)
        editor.apply()

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
