package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TourAdapter(
    private val tours: List<Tour>,
    private val isTourAgent: Boolean,
    private val context: Context,
    private val isProfileView: Boolean,
    private val tourUpdateListener: TourUpdateListener // Передаем интерфейс
) : RecyclerView.Adapter<TourAdapter.TourViewHolder>() {

    private lateinit var userDao: UserDao

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tour, parent, false)
        userDao = AppDatabase.getDatabase(parent.context).userDao()
        return TourViewHolder(view)
    }

    override fun onBindViewHolder(holder: TourViewHolder, position: Int) {
        val tour = tours[position]
        holder.tvTourName.text = tour.name
        holder.tvCountry.text = tour.country
        holder.tvDescription.text = tour.description
        holder.tvCost.text = "Стоимость: ${tour.cost}"

        if (isProfileView) {
            holder.btnBuyTour.visibility = View.GONE
            if (!isTourAgent) {
                holder.btnCancelTour.visibility = View.VISIBLE
            }
        } else {
            holder.btnBuyTour.visibility = if (isTourAgent) View.GONE else View.VISIBLE
            holder.btnCancelTour.visibility = View.GONE
        }

        holder.btnBuyTour.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val currentUser = userDao.getCurrentLoggedInUser()
                if (currentUser != null && !isTourAgent) {
                    val crossRef = UserTourCrossRef(currentUser.id, tour.id)
                    userDao.insertUserTourCrossRef(crossRef)
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Тур куплен!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        holder.btnCancelTour.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val currentUser = userDao.getCurrentLoggedInUser()
                if (currentUser != null) {
                    userDao.deleteUserTourCrossRef(currentUser.id, tour.id)
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Вы отказались от тура!", Toast.LENGTH_SHORT).show()
                        tourUpdateListener.onTourListUpdated() // Вызов интерфейса
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = tours.size

    class TourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTourName: TextView = itemView.findViewById(R.id.tvTourName)
        val tvCountry: TextView = itemView.findViewById(R.id.tvCountry)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvCost: TextView = itemView.findViewById(R.id.tvCost)
        val btnBuyTour: Button = itemView.findViewById(R.id.btnBuyTour)
        val btnCancelTour: Button = itemView.findViewById(R.id.btnCancelTour)
    }
}