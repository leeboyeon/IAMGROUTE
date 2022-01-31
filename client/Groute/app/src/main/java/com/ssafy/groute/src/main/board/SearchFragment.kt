package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentSearchBinding
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.home.PlaceViewModel
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking

private const val TAG = "SearchFragment"
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::bind, R.layout.fragment_search) {
    private lateinit var mainActivity: MainActivity
    private lateinit var searchAdapter:SearchAdapter
    private var searchList:List<Place> = arrayListOf()
    private val placeViewModel: PlaceViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(true)
        arguments?.let {

        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = placeViewModel

        runBlocking {
            placeViewModel.getPlaceList()
        }
        initButton()
        initAdapter()
    }

    fun initAdapter(){

        placeViewModel.placeList.observe(viewLifecycleOwner, Observer {
            searchAdapter = SearchAdapter(it, requireContext())
            searchAdapter.setItemClickListener(object : SearchAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, id: Int) {
                    mainActivity.moveFragment(8,"placeId", it[position].id)
                }

            })
            binding.boardSearchRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                adapter  = searchAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        })

        binding.boardSearchSv.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange: $newText")
                if(TextUtils.isEmpty(newText)){
                    searchAdapter.filter.filter("")
                }else{
                    searchAdapter.filter.filter(newText.toString())
                    searchAdapter.notifyDataSetChanged()
                }

                return false
            }

        })
    }
    fun initButton(){
        binding.boardSearchIbtnCancle.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(value: String, key: Int) =
            SearchFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}

