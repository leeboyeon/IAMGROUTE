package com.ssafy.groute.src.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentHomeBinding
import com.ssafy.groute.databinding.FragmentRouteBinding
import com.ssafy.groute.src.main.MainActivity


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var categoryAdapter:CategoryAdapter = CategoryAdapter()
    private var bestrouteAdatper:BestRouteAdapter = BestRouteAdapter()


    private lateinit var mainActivity : MainActivity
    val catelists = mutableListOf<Category>()
    val bests = mutableListOf<BestRoute>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdatper()
    }
    fun initAdatper(){
        categoryAdapter = CategoryAdapter()
        bestrouteAdatper = BestRouteAdapter()
        bests.apply {
            add(BestRoute(img=R.drawable.normalimg, title="[대구] 3박 4일 뭐하고 놀지?!"))
            add(BestRoute(img=R.drawable.normalimg, title="[대구] 3박 4일 뭐하고 놀지?!"))
            add(BestRoute(img=R.drawable.normalimg, title="[대구] 3박 4일 뭐하고 놀지?!"))

            bestrouteAdatper.list = bests
            bestrouteAdatper.notifyDataSetChanged()
        }
        catelists.apply {
            add(Category(img = R.drawable.jeju, name="제주"))
            add(Category(img = R.drawable.busan, name="부산"))
            add(Category(img = R.drawable.kangwondo, name="강원"))
            add(Category(img = R.drawable.keungju, name="경주"))
            add(Category(img = R.drawable.chungbuk, name="충북"))
            add(Category(img = R.drawable.keongkido, name="경기"))
            add(Category(img = R.drawable.deagu, name="대구"))
            add(Category(img = R.drawable.yeosu, name="여수"))
            add(Category(img = R.drawable.jeonju, name="전주"))
            add(Category(img = R.drawable.incheon, name="인천"))

            categoryAdapter.list = catelists
            categoryAdapter.notifyDataSetChanged()
        }
        bestrouteAdatper.setItemClickListener(object : BestRouteAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, name: String) {
                //event
            }
        })
        categoryAdapter.setItemClickListener(object: CategoryAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, name: String) {
                //event
            }
        })
        binding.homeRvBestRoute.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter = bestrouteAdatper
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        binding.homeRvCategory.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = categoryAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {

            }
    }
}