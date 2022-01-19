package com.ssafy.groute.src.main.travel

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R

class TravelPlanListRvHelperCallback(private val recyclerviewAdapter: TravelPlanListRecyclerviewAdapter) : ItemTouchHelper.Callback() {

    // 리싸이클러뷰 아이템을 swipe 했을 때 삭제 화면이 보이도록 고정하기 위한 변수들
    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f

    // 이동 방향 결정
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(UP or DOWN, LEFT or RIGHT)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        // 리싸이클러뷰에서 현재 선택된 아이템과 드래그한 위치에 있는 아이템을 교환
        val fromPos: Int = viewHolder.absoluteAdapterPosition
        val toPos: Int = target.absoluteAdapterPosition
        recyclerviewAdapter.swapData(fromPos, toPos)
        return true
    }

    // 스와이프 할 때 처리
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        currentDx = 0f
        previousPosition = viewHolder.absoluteAdapterPosition
        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

    }

    private fun getView(viewHolder: RecyclerView.ViewHolder) : View = viewHolder.itemView.findViewById(R.id.item_board_layout)
}