package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteTourActivity : AppCompatActivity() {

    private lateinit var tourDao: TourDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_tour)

        tourDao = AppDatabase.getDatabase(this).tourDao()

        val container: LinearLayout = findViewById(R.id.buttonContainer)
        loadTours(container)
    }

    private fun loadTours(container: LinearLayout) {
        CoroutineScope(Dispatchers.IO).launch {
            val tours = tourDao.getAllTours()
            runOnUiThread {
                for (tour in tours) {
                    val button = Button(this@DeleteTourActivity)
                    button.text = tour.name
                    button.setOnClickListener {
                        deleteTour(tour.id)
                    }
                    container.addView(button)
                }
            }
        }
    }

    private fun deleteTour(tourId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val tour = tourDao.getTourById(tourId)
            if (tour != null) {
                tourDao.deleteTour(tour)
                runOnUiThread {
                    Toast.makeText(this@DeleteTourActivity, "Тур удалён", Toast.LENGTH_SHORT).show()
                    finish() // Закрыть активность после удаления
                }
            }
        }
    }
}