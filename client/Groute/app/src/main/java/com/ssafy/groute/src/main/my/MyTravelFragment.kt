package com.ssafy.groute.src.main.my

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentMyTravelBinding
import com.ssafy.groute.src.main.MainActivity

class MyTravelFragment : Fragment() {
    private lateinit var binding: FragmentMyTravelBinding
    private var mytravelAdapter:MyTravelAdapter = MyTravelAdapter()
    private lateinit var mainActivity:MainActivity

    val ing = mutableListOf<MyTravel>()
    val ed = mutableListOf<MyTravel>()
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
        binding = FragmentMyTravelBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }
    fun initAdapter(){
        mytravelAdapter = MyTravelAdapter()
        ing.apply {
            add(MyTravel(img=R.drawable.busan, title="[부산] 지우와 함께하는 부산여행", due="2022.01.18-01.80"))
            add(MyTravel(img=R.drawable.busan, title="[부산] 지우와 함께하는 부산여행", due="2022.01.18-01.80"))
            add(MyTravel(img=R.drawable.busan, title="[부산] 지우와 함께하는 부산여행", due="2022.01.18-01.80"))
            mytravelAdapter.list = ing
            mytravelAdapter.notifyDataSetChanged()
        }
        binding.mytravelRvIng.apply{
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = mytravelAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        mytravelAdapter = MyTravelAdapter()
        ed.apply {
            add(MyTravel(img=R.drawable.incheon, title="[인천] 경희님와 함께하는 월미도여행", due="2021.12.18-12.20"))
            add(MyTravel(img=R.drawable.incheon, title="[인천] 경희님와 함께하는 월미도여행", due="2021.12.18-12.20"))
            mytravelAdapter.list = ed
            mytravelAdapter.notifyDataSetChanged()
        }
        binding.mytravelRvMylist.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = mytravelAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyTravelFragment().apply {

            }
    }
}