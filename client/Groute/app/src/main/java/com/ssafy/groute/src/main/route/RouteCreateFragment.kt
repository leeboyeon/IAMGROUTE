package com.ssafy.groute.src.main.route

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentRouteCreateBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.dto.Area
import com.ssafy.groute.src.main.home.CategoryAdapter
import com.ssafy.groute.src.service.AreaService
import com.ssafy.groute.util.RetrofitCallback


private const val TAG = "RouteCreateFragment"
class RouteCreateFragment : Fragment() {
    private lateinit var binding: FragmentRouteCreateBinding
    private lateinit var mainActivity:MainActivity

    private var categoryAdapter:CategoryAdapter = CategoryAdapter()
    private var memberAdapter:MemberAdapter = MemberAdapter()

    val area = mutableListOf<Area>()
    val member = mutableListOf<Member>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)
        mainActivity.hideMainProfileBar(true)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRouteCreateBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        binding.rcBtnSave.setOnClickListener {
            mainActivity.moveFragment(7)
        }
    }
    fun initAdapter(){
        getData()


        memberAdapter = MemberAdapter()
        member.apply {
            add(Member(img=R.drawable.user, name="송경희님"))
            memberAdapter.list = member
            memberAdapter.notifyDataSetChanged()
        }

        binding.routeRvMember.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = memberAdapter
            adapter!!.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

    }
    fun getData(){
//        AreaService().getAreas(AreaCallback())
    }
    inner class AreaCallback: RetrofitCallback<List<Area>> {

        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: $t")
        }

        override fun onSuccess(code: Int, responseData: List<Area>) {
            Log.d(TAG, "onSuccess: ${responseData}")
            responseData.let{
                categoryAdapter = CategoryAdapter()
                categoryAdapter.list = responseData
                categoryAdapter.setItemClickListener(object : CategoryAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, name: String) {
                        mainActivity.moveFragment(3)
                    }
                })
            }
            binding.routecreateRvArea.apply {
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                adapter = categoryAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }

    }
}