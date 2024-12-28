package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TourListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tourDao: TourDao
    private lateinit var userDao: UserDao
    private lateinit var currentUser: User
    private var showFiltered: Boolean = false
    private var tourUpdateListener: TourUpdateListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TourUpdateListener) {
            tourUpdateListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            showFiltered = it.getBoolean(ARG_SHOW_FILTERED, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tour_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewTours)
        recyclerView.layoutManager = LinearLayoutManager(context)

        tourDao = AppDatabase.getDatabase(requireContext()).tourDao()
        userDao = AppDatabase.getDatabase(requireContext()).userDao()

        CoroutineScope(Dispatchers.IO).launch {
            currentUser = userDao.getCurrentLoggedInUser() ?: return@launch
            loadTours()
        }
    }

    private suspend fun loadTours() {
        val tours = if (showFiltered) {
            if (currentUser.isTourAgent) {
                tourDao.getToursByAgent(currentUser.id)
            } else {
                val userWithTours = userDao.getUserWithTours(currentUser.id)
                userWithTours?.tours ?: listOf()
            }
        } else {
            tourDao.getAllTours()
        }

        activity?.runOnUiThread {
            if (tours.isNotEmpty()) {
                recyclerView.adapter = TourAdapter(tours, currentUser.isTourAgent, requireContext(), showFiltered, tourUpdateListener!!)
            } else {
                Toast.makeText(requireContext(), "Туры не найдены", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val ARG_SHOW_FILTERED = "show_filtered"

        @JvmStatic
        fun newInstance(showFiltered: Boolean) =
            TourListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOW_FILTERED, showFiltered)
                }
            }
    }
}