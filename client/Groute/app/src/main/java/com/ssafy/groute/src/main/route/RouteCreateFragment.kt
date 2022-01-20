package com.ssafy.groute.src.main.route

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentRouteCreateBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.home.Category


class RouteCreateFragment : Fragment() {
    private lateinit var binding: FragmentRouteCreateBinding
    private lateinit var mainActivity:MainActivity

    private var areaAdapter:AreaAdapter = AreaAdapter()
    private var memberAdapter:MemberAdapter = MemberAdapter()

    val area = mutableListOf<Category>()
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
            mainActivity.openFragment(7)
        }
    }
    fun initAdapter(){
        areaAdapter = AreaAdapter()
        area.apply {
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

            areaAdapter.list = area
            areaAdapter.notifyDataSetChanged()
        }

        binding.routecreateRvArea.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = areaAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

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

}