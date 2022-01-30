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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.databinding.FragmentSearchBinding
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.util.RetrofitCallback

private const val TAG = "SearchFragment"
class SearchFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter:SearchAdapter
    private var searchList:List<Place> = arrayListOf()
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
        initAdapter()
    }
    fun initAdapter(){
        PlaceService().getPlaces(PlaceCallback())

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

    inner class PlaceCallback: RetrofitCallback<List<Place>> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: List<Place>) {
//            Log.d(TAG, "onSuccess: ${responseData}")
            responseData.let{
                searchAdapter = SearchAdapter(responseData,requireContext())
                searchAdapter.setItemClickListener(object : SearchAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, placeId: Int) {
                        mainActivity.moveFragment(8,"placeId", placeId)
                    }

                })
            }
            binding.boardSearchRv.apply{
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                adapter = searchAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }


        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
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

