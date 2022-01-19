package com.ssafy.groute.src.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentAreaBinding
import com.ssafy.groute.src.main.MainActivity


class AreaFragment : Fragment() {
    private lateinit var binding: FragmentAreaBinding
    private lateinit var mainActivity:MainActivity
    private lateinit var areaFilterAdapter:AreaFilterAdapter

    val lists = arrayListOf<Place>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        // Inflate the layout for this fragment
        binding= FragmentAreaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTab()

    }
    fun initAdapter(){
        areaFilterAdapter = AreaFilterAdapter(lists)
        lists.apply{
            add(Place(
                img =R.drawable.jejuplace1,
                name ="새별오름",
                content ="황금빛 억새가 휘날리는 아름다운 풍경을 마주할 수 있는 오름",
                review =3.0,
                info = "관광명소 | 애월한림")
            )
            add(Place(
                img =R.drawable.jejuplace2,
                name ="아르떼 뮤지엄",
                content ="시각정 강렬함을 선사하는 국내 최대 규모의 미디어아트 전시관",
                review =3.0,
                info = "관광명소 | 애월한림")
            )
            add(Place(
                img =R.drawable.jejuplace3,
                name ="샤려니 숲길",
                content ="시원한 나무 그늘아래, 맑은 공기를 마시며 걷기 좋은 숲길",
                review =4.5,
                info = "관광명소 | 제주서귀포시")
            )
            add(Place(
                img =R.drawable.jejuplace4,
                name ="카멜리아힐",
                content ="세계 80개국의 동백나무를 볼 수 있는 동백수목원",
                review =4.5,
                info = "관광명소 | 중문")
            )
            add(Place(
                img =R.drawable.jejucafe1,
                name ="원앤온리",
                content ="'산방산'의 이국적인 뷰를 감상할 수 있는 카페",
                review =4.7,
                info = "카페 | 중문")
            )
            add(Place(
                img =R.drawable.jejucafe2,
                name ="우무",
                content ="우뭇가사리로 만든 수제 푸딩을 선보이는 테이크아웃 전문점",
                review =4.9,
                info = "카페 | 애월한림")
            )
            add(Place(
                img =R.drawable.jejueat1,
                name ="우진 해장국",
                content ="'수요미식회'에 방영된 따끈한 국물 요리로 해장하기 좋은 음식점",
                review =4.5,
                info = "맛집 | 제주 시내")
            )
            add(Place(
                img =R.drawable.jejueat3,
                name ="수 우동",
                content ="여러 방송에 출연해 도민들도 즐겨 찾는 맛집",
                review =4.1,
                info = "맛집 | 애월한림")
            )
            add(Place(
                img =R.drawable.jejuexcite1,
                name ="에코랜드",
                content ="약 30만 평의 숲속을 기차로 다니며 다양한 체험을 할 수 있는 곳",
                review =4.4,
                info = "테마/체험 | 애월한림")
            )
            add(Place(
                img =R.drawable.jejuexcite2,
                name ="김녕 미로 공원",
                content ="사시사철 푸른 나무로 이루어진 거대한 미로가 위치한 공원",
                review =4.7,
                info = "테마/체험 | 조천구좌")
            )
            add(Place(
                img =R.drawable.jejuexcite3,
                name ="9.81 파크",
                content ="국내 최초의 무동력 레이싱 테마 파크",
                review = 3.5,
                info = "테마/체험 | 애월한림")
            )
            add(Place(
                img =R.drawable.jejushop1,
                name ="제주 동문시장",
                content ="먹거리와 생활용품을 판매하는 제주에서 가장 큰 시장",
                review = 3.8,
                info = "쇼핑 | 제주 시내")
            )
            add(Place(
                img =R.drawable.jejushop2,
                name ="디 앤 디파트먼트",
                content ="힙스터들이 사랑하는 글로벌 편집숍",
                review = 4.2,
                info = "쇼핑 | 제주 시내")
            )

            areaFilterAdapter.list = lists
            areaFilterAdapter.notifyDataSetChanged()
        }

        binding.areaRvPlaceitem.apply{
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = areaFilterAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        areaFilterAdapter.setItemClickListener(object : AreaFilterAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, name: String) {
                mainActivity.openFragment(4)
            }

        })

    }
    fun initTab(){
        initAdapter()
        binding.areaTabLayout.addTab(binding.areaTabLayout.newTab().setText("쇼핑"))
        binding.areaTabLayout.addTab(binding.areaTabLayout.newTab().setText("카페"))
        binding.areaTabLayout.addTab(binding.areaTabLayout.newTab().setText("맛집"))

        binding.areaTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 ->{
                        areaFilterAdapter.filter.filter("")
                    }
                    1->{
                        areaFilterAdapter.filter.filter("관광명소")
                    }
                    2->{
                        areaFilterAdapter.filter.filter("테마/체험")
                    }
                    3->{
                        areaFilterAdapter.filter.filter("쇼핑")
                    }
                    4->{
                        areaFilterAdapter.filter.filter("맛집")
                    }
                    5->{
                        areaFilterAdapter.filter.filter("카페")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AreaFragment().apply {

            }
    }
}